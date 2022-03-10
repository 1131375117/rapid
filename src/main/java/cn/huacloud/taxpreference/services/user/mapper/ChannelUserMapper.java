package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.common.enums.user.ChannelUserType;
import cn.huacloud.taxpreference.services.user.entity.dos.ChannelUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

/**
 * @author wangkh
 */
@Repository
public interface ChannelUserMapper extends BaseMapper<ChannelUserDO> {

    default ChannelUserDO getByOpenUserIdAndChannelType(String openUserId, ChannelUserType channelType) {
        LambdaQueryWrapper<ChannelUserDO> queryWrapper = Wrappers.lambdaQuery(ChannelUserDO.class)
                .eq(ChannelUserDO::getOpenUserId, openUserId)
                .eq(ChannelUserDO::getChannelType, channelType)
                .eq(ChannelUserDO::getDeleted, false);
        return selectOne(queryWrapper);
    }
}
