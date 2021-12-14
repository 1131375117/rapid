package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dtos.DocStatisticsPlus;
import cn.huacloud.taxpreference.services.common.watch.WatcherViewService;
import cn.huacloud.taxpreference.services.consumer.CollectionService;
import cn.huacloud.taxpreference.services.consumer.entity.dos.CollectionDO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.CollectionDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.CollectionVO;
import cn.huacloud.taxpreference.services.consumer.mapper.CollectionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
        Boolean flag;
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
    public PageVO<CollectionVO> queryCollection(PageQueryDTO pageQueryDTO, Long userId) {
        pageQueryDTO.paramReasonable();
        LambdaQueryWrapper<CollectionDO> collectionDOLambdaQueryWrapper = Wrappers.lambdaQuery(CollectionDO.class);
        collectionDOLambdaQueryWrapper.eq(CollectionDO::getConsumerUserId, userId);
        IPage<CollectionDO> queryPage = pageQueryDTO.createQueryPage();
        IPage<CollectionDO> collectionDOIPage = collectionMapper.selectPage(queryPage, collectionDOLambdaQueryWrapper);
        List<CollectionVO> collectionVOList = collectionDOIPage
                .getRecords().stream().map(collectionDO -> {
                    CollectionVO collectionVO = new CollectionVO();
                    BeanUtils.copyProperties(collectionDO, collectionVO);
                    return collectionVO;
                }).collect(Collectors.toList());

        return PageVO.createPageVO(collectionDOIPage, collectionVOList);
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
