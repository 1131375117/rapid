package cn.huacloud.taxpreference.services.user.impl;

import cn.huacloud.taxpreference.common.enums.user.ChannelUserType;
import cn.huacloud.taxpreference.services.user.ChannelUserService;
import cn.huacloud.taxpreference.services.user.ConsumerUserService;
import cn.huacloud.taxpreference.services.user.entity.dos.ChannelUserDO;
import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import cn.huacloud.taxpreference.services.user.mapper.ChannelUserMapper;
import cn.huacloud.taxpreference.services.user.mapper.ConsumerUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class ChannelUserServiceImpl implements ChannelUserService {

    private final ConsumerUserService consumerUserService;

    private final ChannelUserMapper channelUserMapper;

    @Transactional
    @Override
    public ConsumerUserDO saveOrReturnByOpenUserIdAndChannelType(String openUserId, ChannelUserType channelType, String... extendsFields) {
        // 查找渠道用户是否存在
        ChannelUserDO channelUserDO = channelUserMapper.getByOpenUserIdAndChannelType(openUserId, channelType);
        if (channelUserDO != null) {
            return consumerUserService.getUserDOById(channelUserDO.getConsumerUserId());
        }
        // 不存在需要新创建用户
        ConsumerUserDO consumerUserDO = consumerUserService.autoCreateUserByOpenUserId(channelType.defaultUsername, openUserId);

        channelUserDO = new ChannelUserDO()
                .setConsumerUserId(consumerUserDO.getId())
                .setChannelName(channelType.name)
                .setChannelType(channelType)
                .setOpenUserId(openUserId)
                .setCreateTime(LocalDateTime.now())
                .setDeleted(false);
        if (extendsFields.length > 0) {
            channelUserDO.setExtendsField1(extendsFields[0]);
        }
        if (extendsFields.length > 1) {
            channelUserDO.setExtendsField2(extendsFields[2]);
        }
        channelUserMapper.insert(channelUserDO);
        return consumerUserDO;
    }
}
