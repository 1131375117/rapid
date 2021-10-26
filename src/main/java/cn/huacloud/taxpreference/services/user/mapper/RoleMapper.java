package cn.huacloud.taxpreference.services.user.mapper;

import cn.huacloud.taxpreference.services.user.entity.dos.RoleDO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleListVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Repository;

/**
 * 权限数据操作
 * @author wangkh
 */
@Repository
public interface RoleMapper extends BaseMapper<RoleDO> {

    /**
     * 角色分页列表
     * @param queryPage 分页查询条件
     * @return 角色分页列表
     */
    IPage<RoleListVO> rolePageQuery(IPage<Object> queryPage);

    /**
     * 查看角色码值是否存在
     * @param roleCode 角色码值
     * @return 是否存在
     */
    default boolean isRoleCodeExist(String roleCode) {
        LambdaQueryWrapper<RoleDO> queryWrapper = Wrappers.lambdaQuery(RoleDO.class)
                .eq(RoleDO::getRoleCode, roleCode);
        Long count = selectCount(queryWrapper);
        return count > 0;
    }
}
