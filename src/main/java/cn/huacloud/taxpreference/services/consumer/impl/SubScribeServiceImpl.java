package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.watch.WatcherViewService;
import cn.huacloud.taxpreference.services.consumer.SubScribeService;
import cn.huacloud.taxpreference.services.consumer.entity.dos.SubscribeDO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.SubScribeDTO;
import cn.huacloud.taxpreference.services.consumer.mapper.SubscribeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订阅服务实现类
 *
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class SubScribeServiceImpl implements SubScribeService {

    private final SubscribeMapper subscribeMapper;
    private final DocStatisticsService docStatisticsService;
    private final SysParamService sysParamService;
    private final WatcherViewService watchViewService;


    @Override
    public Boolean saveOrCancelSubscribe(SubScribeDTO subScribeDTO) {
        LambdaQueryWrapper<SubscribeDO> wrapper = Wrappers.lambdaQuery(SubscribeDO.class)
                .eq(SubscribeDO::getConsumerUserId, subScribeDTO.getConsumerUserId())
                .eq(SubscribeDO::getSourceId, subScribeDTO.getSourceId())
                .eq(SubscribeDO::getSubscribeType, subScribeDTO.getSubScribeType());
        SubscribeDO subscribeDO = subscribeMapper.selectOne(wrapper);

        if (subscribeDO == null) {
            subscribeDO = new SubscribeDO();
            BeanUtils.copyProperties(subScribeDTO, subscribeDO);
            subscribeDO.setCreateTime(LocalDateTime.now());
            subscribeDO.setSubscribeType(subScribeDTO.getSubScribeType().getValue());
            subscribeMapper.insert(subscribeDO);
            return true;
        } else {
            subscribeMapper.delete(wrapper);
            return false;
        }

    }

    @Override
    public List<SubscribeDO> selectList(Long id, CollectionType taxPreference) {
        LambdaQueryWrapper<SubscribeDO> wrapper = Wrappers.lambdaQuery(SubscribeDO.class)
                .eq(SubscribeDO::getSourceId, id)
                .eq(SubscribeDO::getSubscribeType, taxPreference);
        List<SubscribeDO> subscribeDOS = subscribeMapper.selectList(wrapper);
        return subscribeDOS;
    }

    @Override
    public Boolean isUserSubscribe(Long currentUserId, CollectionType subscribeType, Long id) {
        LambdaQueryWrapper<SubscribeDO> wrapper = Wrappers.lambdaQuery(SubscribeDO.class)
                .eq(SubscribeDO::getSourceId, id)
                .eq(SubscribeDO::getConsumerUserId,currentUserId)
                .eq(SubscribeDO::getSubscribeType, subscribeType);
        List<SubscribeDO> subscribeDOS = subscribeMapper.selectList(wrapper);
        if (subscribeDOS != null&&subscribeDOS.size()>0) {
            return true;
        }
        return false;
    }

}
