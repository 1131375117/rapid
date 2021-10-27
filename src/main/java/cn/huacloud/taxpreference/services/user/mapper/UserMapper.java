package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.services.user.entity.dos.RoleDO;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户数据操作
 * @author wangkh
 */
@Repository
public interface UserMapper extends BaseMapper<UserDO> {

    /**
     * 根据用户账户查询 UserDO
     * @param userAccount 用户账户
     * @return userDO
     */
    default UserDO getUserDOByAccount(String userAccount) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserAccount, userAccount)
                .eq(UserDO::getDeleted, false);
        return selectOne(queryWrapper);
    }

    /**
     * 根据角色码值查询 UserDO
     * @param roleCode 角色码值
     * @return userDO
     */
    default List<UserDO> getUserDOByRoleCode(String roleCode) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getDeleted, false)
                .apply("FIND_IN_SET ('" + roleCode + "', roleCodes)");
        return selectList(queryWrapper);
    }
}
