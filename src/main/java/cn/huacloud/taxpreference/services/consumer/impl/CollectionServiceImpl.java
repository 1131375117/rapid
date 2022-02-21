package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dtos.CollectionQueryDTO;
import cn.huacloud.taxpreference.services.common.entity.dtos.DocStatisticsPlus;
import cn.huacloud.taxpreference.services.common.watch.WatcherViewService;
import cn.huacloud.taxpreference.services.consumer.CollectionService;
import cn.huacloud.taxpreference.services.consumer.entity.dos.CollectionDO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.CollectionDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.CollectionPageVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.CollectionVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PageByCollectionVO;
import cn.huacloud.taxpreference.services.consumer.mapper.CollectionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.huacloud.taxpreference.services.common.watch.WatcherOperation.TYPE_TRIGGER_MAP;

/**
 * 收藏服务实现类
 *
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionMapper collectionMapper;
    private final DocStatisticsService docStatisticsService;
    private final SysParamService sysParamService;
    private final WatcherViewService watchViewService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrCancelCollection(CollectionDTO collectionDTO) {

        CollectionDO saveCollectionDO = new CollectionDO();
        DocStatisticsPlus docStatisticsPlus = new DocStatisticsPlus()
                .setDocId(collectionDTO.getSourceId())
                .setDocType(DocType.valueOf(collectionDTO.getCollectionType().name));
        BeanUtils.copyProperties(collectionDTO, saveCollectionDO);
        //收藏或取消收藏
        boolean flag;
        if (queryCollection(collectionDTO) != null) {
            collectionMapper.deleteById(queryCollection(collectionDTO));
            docStatisticsPlus.setCollectionsPlus(-1L);
            flag = false;
        } else {
            saveCollectionDO.setCreateTime(LocalDateTime.now());
            collectionMapper.insert(saveCollectionDO);
            docStatisticsPlus.setCollectionsPlus(1L);
            flag = true;
        }
        //保存到统计表
        docStatisticsService.saveOrUpdateDocStatisticsService(docStatisticsPlus);
        //应用到es
        TYPE_TRIGGER_MAP.get(docStatisticsPlus.getDocType()).saveEvent(collectionDTO.getSourceId());
        return flag;
    }

    @Override
    public PageByCollectionVO queryCollection(CollectionQueryDTO pageQueryDTO, Long userId) {

        //根据操作类型查询不同的收藏记录
        Page<CollectionVO> page = new Page<>(pageQueryDTO.getPageNum(), pageQueryDTO.getPageSize());
        IPage<CollectionVO> collectionDOIPage;
        //获取收藏类型
        CollectionType collectionType = pageQueryDTO.getCollectionType();

        if (CollectionType.CASE_ANALYSIS.equals(collectionType)) {
            //案例分析
            collectionDOIPage = collectionMapper.selectCaseAnalysisByCollectionType(page, CollectionType.CASE_ANALYSIS, userId);
        } else if (CollectionType.FREQUENTLY_ASKED_QUESTION.equals(collectionType)) {
            //热门问答
            collectionDOIPage = collectionMapper.selectFrequentlyAskedQuestionByCollectionType(page, CollectionType.FREQUENTLY_ASKED_QUESTION, userId);
        } else if (CollectionType.POLICIES_EXPLAIN.equals(collectionType)) {
            //政策解读
            collectionDOIPage = collectionMapper.selectPoliciesExplainByCollectionType(page, CollectionType.POLICIES_EXPLAIN, userId);
        } else if (CollectionType.TAX_PREFERENCE.equals(collectionType)) {
            //税收优惠
            collectionDOIPage = collectionMapper.selectTaxPreferenceByCollectionType(page, CollectionType.TAX_PREFERENCE, userId);
        } else if (CollectionType.CONSULTATION.equals(collectionType)) {
            //税收优惠
            collectionDOIPage = collectionMapper.selectConSultationByCollectionType(page, CollectionType.CONSULTATION, userId);
        }else {
            //政策法规
            collectionDOIPage = collectionMapper.selectPoliciesByCollectionType(page, CollectionType.POLICIES, userId);
        }
        int maxSize = 200;
        if (collectionDOIPage.getTotal() > maxSize) {
            collectionDOIPage.setTotal(maxSize);
            //获取page
            int maxPage = maxSize % pageQueryDTO.getPageSize() == 0 ? maxSize / pageQueryDTO.getPageSize() : maxSize / pageQueryDTO.getPageSize() + 1;
            if (maxPage == pageQueryDTO.getPageNum()) {
                int lastPageSize = maxSize % pageQueryDTO.getPageSize();
                collectionDOIPage.setRecords(collectionDOIPage.getRecords().stream().limit(lastPageSize).collect(Collectors.toList()));
            }
        }
        PageVO<CollectionVO> pageVO = PageVO.createPageVO(collectionDOIPage, collectionDOIPage.getRecords());
        List<CollectionPageVO> collectionPageVOList = new ArrayList<>();
        Set<LocalDate> dateSet = pageVO.getRecords().stream().map(CollectionVO::getCollectionTime).collect(Collectors.toSet());
        dateSet.forEach(localDate -> {
            //返回的vo对象
            CollectionPageVO<CollectionVO> collectionPageVO = new CollectionPageVO();
            List<CollectionVO> collectionVOList = new ArrayList<>();
            collectionPageVO.setDate(localDate);
            pageVO.getRecords().forEach(collectionVO -> {
                if(collectionVO.getCollectionTime().isEqual(localDate)){
                    collectionVOList.add(collectionVO);
                }
            });
            collectionPageVO.setPageVOList(collectionVOList);
            collectionPageVOList.add(collectionPageVO);
        });
        PageByCollectionVO pageByCollectionVO = new PageByCollectionVO();
        BeanUtils.copyProperties(pageVO, pageByCollectionVO);
        pageByCollectionVO.setList(collectionPageVOList);
        return pageByCollectionVO;
    }

    @Override
    public Boolean isUserCollection(Long consumerUserId, CollectionType collectionType, Long sourceId) {
        LambdaQueryWrapper<CollectionDO> queryWrapper = Wrappers.lambdaQuery(CollectionDO.class)
                .eq(CollectionDO::getConsumerUserId, consumerUserId)
                .eq(CollectionDO::getCollectionType, collectionType)
                .eq(CollectionDO::getSourceId, sourceId);
        Long count = collectionMapper.selectCount(queryWrapper);
        return count > 0;
    }

    /**
     * 查询收藏
     */
    private CollectionDO queryCollection(CollectionDTO collectionDtO) {
        LambdaQueryWrapper<CollectionDO> queryWrapper = Wrappers.lambdaQuery(CollectionDO.class)
                .eq(CollectionDO::getConsumerUserId, collectionDtO.getConsumerUserId())
                .eq(CollectionDO::getCollectionType, collectionDtO.getCollectionType())
                .eq(CollectionDO::getSourceId, collectionDtO.getSourceId());
        return collectionMapper.selectOne(queryWrapper);
    }
}
