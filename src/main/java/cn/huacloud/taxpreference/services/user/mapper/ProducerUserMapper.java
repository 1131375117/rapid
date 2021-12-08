package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.services.user.entity.dos.ProducerUserDO;
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
public interface ProducerUserMapper extends BaseMapper<ProducerUserDO> {

    /**
     * 根据用户账户查询 UserDO
     * @param userAccount 用户账户
     * @return producerUserDO
     */
    default ProducerUserDO getUserDOByAccount(String userAccount) {
        LambdaQueryWrapper<ProducerUserDO> queryWrapper = Wrappers.lambdaQuery(ProducerUserDO.class)
                .eq(ProducerUserDO::getUserAccount, userAccount)
                .eq(ProducerUserDO::getDeleted, false);
        return selectOne(queryWrapper);
    }

    /**
     * 根据角色码值查询 UserDO
     * @param roleCode 角色码值
     * @return producerUserDO
     */
    default List<ProducerUserDO> getUserDOByRoleCode(String roleCode) {
        LambdaQueryWrapper<ProducerUserDO> queryWrapper = Wrappers.lambdaQuery(ProducerUserDO.class)
                .eq(ProducerUserDO::getDeleted, false)
                .apply("FIND_IN_SET ('" + roleCode + "', role_Codes)");
        return selectList(queryWrapper);
    }

    /**
     * 根据用户账户查询 UserDO包括被删除
     * @param userAccount 用户账户
     * @return producerUserDO
     */
    default ProducerUserDO getUserDOByAccountWithDelete(String userAccount) {
        LambdaQueryWrapper<ProducerUserDO> queryWrapper = Wrappers.lambdaQuery(ProducerUserDO.class)
                .eq(ProducerUserDO::getUserAccount, userAccount);
        return selectOne(queryWrapper);
    }
}
