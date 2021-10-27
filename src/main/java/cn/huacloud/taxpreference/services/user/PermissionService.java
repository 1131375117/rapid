package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.services.user.entity.vos.PermissionGroupVO;

import java.util.List;
import java.util.Set;

/**
 * 权限服务
 * @author wangkh
 */
public interface PermissionService {

    /**
     * 获取权限分组列表
     * @return 权限分组列表
     */
    List<PermissionGroupVO> getPermissionGroupVOList();

    /**
     * 获取所有权限码值
     * @return 权限码值结合
     */
    Set<String> getAllPermissionCodeSet();
}
