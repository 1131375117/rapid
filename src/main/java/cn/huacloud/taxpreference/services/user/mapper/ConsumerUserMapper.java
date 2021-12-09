package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 消费者用户数据操作对象
 * @author wangkh
 */
@Repository
public interface ConsumerUserMapper extends BaseMapper<ConsumerUserDO> {
}
