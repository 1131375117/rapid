package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.services.user.entity.vos.RoleVO;

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
     */
    Set<String> getAllRoleCodes();
}
