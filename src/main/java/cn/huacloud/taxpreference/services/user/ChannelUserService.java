package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.common.enums.user.ChannelUserType;
import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;

/**
 * @author wangkh
 */
public interface ChannelUserService {
    /**
     * 创建或返回渠道用户
     * @param openUserId 开放用户ID
     * @param channelType 渠道类型
     * @return 消费者用户实体
     */
    ConsumerUserDO saveOrReturnByOpenUserIdAndChannelType(String openUserId, ChannelUserType channelType, String ... extendsFields);
}
