package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
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

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrCancelCollection(CollectionDTO collectionDtO) {

        //保存收藏
        CollectionDO collectionDO = new CollectionDO();
        DocStatisticsDO docStatisticsDO = new DocStatisticsDO()
                .setDocId(collectionDtO.getSourceId())
                .setDocType(DocType.valueOf(collectionDtO.getCollectionType().name));
        BeanUtils.copyProperties(collectionDtO, collectionDO);

        if (getStatus(collectionDtO)) {
            collectionMapper.deleteById(collectionDO);
            docStatisticsDO.setCollections(-1L);
        } else {
            collectionMapper.insert(collectionDO);
            docStatisticsDO.setCollections(1L);
        }
        //保存到统计表
        docStatisticsService.saveOrUpdateDocStatisticsService(docStatisticsDO);
    }

    @Override
    public PageVO<CollectionVO> queryCollection(PageQueryDTO pageQueryDTO) {

        LambdaQueryWrapper<CollectionDO> collectionDOLambdaQueryWrapper = Wrappers.lambdaQuery(CollectionDO.class);
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

    private Boolean getStatus(CollectionDTO collectionDtO) {
        LambdaQueryWrapper<CollectionDO> queryWrapper = Wrappers.lambdaQuery(CollectionDO.class)
                .eq(CollectionDO::getConsumerUserId, collectionDtO.getConsumerUserId())
                .eq(CollectionDO::getCollectionType, collectionDtO.getCollectionType())
                .eq(CollectionDO::getSourceId, collectionDtO.getSourceId());
        Long count = collectionMapper.selectCount(queryWrapper);
        return count != 0;
    }
}
