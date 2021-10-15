package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 用户
 * @author wangkh
 */
@Repository
public interface UserMapper extends BaseMapper<UserDO> {
}
