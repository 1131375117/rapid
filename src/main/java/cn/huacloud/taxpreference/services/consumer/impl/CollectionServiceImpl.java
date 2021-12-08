package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrCancelCollection(CollectionDTO collectionDtO) {
        CollectionDO collectionDO = new CollectionDO();
        BeanUtils.copyProperties(collectionDtO, collectionDO);
        if (getStatus(collectionDtO)) {
            collectionMapper.deleteById(collectionDO);
        } else {
            collectionMapper.insert(collectionDO);
        }
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
        return collectionDtO.getStatus();
    }
}
