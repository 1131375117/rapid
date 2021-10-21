package cn.huacloud.taxpreference.services.user.impl;

import cn.huacloud.taxpreference.services.user.RoleService;
import cn.huacloud.taxpreference.services.user.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 权限服务实现
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
}
