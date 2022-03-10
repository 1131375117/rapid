package cn.huacloud.taxpreference.services.user.impl;

import cn.huacloud.taxpreference.services.user.ChannelUserService;
import cn.huacloud.taxpreference.services.user.ConsumerUserService;
import cn.huacloud.taxpreference.services.user.mapper.ChannelUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class ChannelUserServiceImpl implements ChannelUserService {

    private final ConsumerUserService consumerUserService;

    private final ChannelUserMapper channelUserMapper;


}
