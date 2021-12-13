package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

/**
 * 消费者用户数据操作对象
 * @author wangkh
 */
@Repository
public interface ConsumerUserMapper extends BaseMapper<ConsumerUserDO> {

    default boolean isPhoneNumberRegister(String phoneNumber) {
        LambdaQueryWrapper<ConsumerUserDO> queryWrapper = Wrappers.lambdaQuery(ConsumerUserDO.class)
                .eq(ConsumerUserDO::getPhoneNumber, phoneNumber)
                .eq(ConsumerUserDO::getDeleted, false);
        return selectCount(queryWrapper) > 0;
    }

    default ConsumerUserDO getByPhoneNumber(String phoneNumber) {
        LambdaQueryWrapper<ConsumerUserDO> queryWrapper = Wrappers.lambdaQuery(ConsumerUserDO.class)
                .eq(ConsumerUserDO::getPhoneNumber, phoneNumber)
                .eq(ConsumerUserDO::getDeleted, false);
        return selectOne(queryWrapper);
    }
}
