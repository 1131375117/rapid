package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.UserType;
import cn.huacloud.taxpreference.services.user.entity.dos.RoleDO;
import cn.huacloud.taxpreference.services.user.entity.dtos.RoleQueryDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleListVO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 角色服务
 * @author wangkh
 */
public interface RoleService {

    /**
     * 获取所有的角色视图
     * @return 角色视图 list
     */
    List<RoleVO> getAllRoleVO();

    /**
     * 获取角色视图 map
     * @return key roleCode value roleVO
     */
    Map<String, RoleVO> getAllRoleVOMap();

    /**
     * 获取所有的角色码值
     * @return 角色码值集合
     * @param userType 用户类型
     */
    Set<String> getAllRoleCodes(UserType userType);

    /**
     * 根据角色码值结合
     * @param roleCodes 角色码值集合
     * @return
     */
    List<RoleDO> getRoleDOByRoleCodes(Collection<String> roleCodes);

    /**
     * 角色分页列表
     * @param roleQueryDTO 分页查询条件
     * @return 角色分页列表
     */
    PageVO<RoleListVO> rolePageQuery(RoleQueryDTO roleQueryDTO);

    /**
     * 添加角色
     * @param roleVO
     */
    void saveRole(RoleVO roleVO);

    /**
     * 修改用户
     * @param roleVO
     */
    void updateRole(RoleVO roleVO);

    /**
     * 删除角色
     * @param roleId
     */
    void deleteRole(String roleId);

    /**
     * 给角色赋予权限
     * @param roleId
     * @param permissionCodes
     */
    void setPermissionToRole(Long roleId, List<String> permissionCodes);
}
