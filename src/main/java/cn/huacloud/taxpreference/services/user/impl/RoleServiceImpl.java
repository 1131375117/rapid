package cn.huacloud.taxpreference.services.user.impl;

import cn.huacloud.taxpreference.services.user.RoleService;
import cn.huacloud.taxpreference.services.user.entity.dos.RoleDO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleVO;
import cn.huacloud.taxpreference.services.user.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public List<RoleVO> getAllRoleVO() {
        return roleMapper.selectList(null).stream()
                .map(roleDO -> {
                    RoleVO roleVO = new RoleVO();
                    BeanUtils.copyProperties(roleDO, roleVO);
                    return roleVO;
                }).collect(Collectors.toList());
    }

    @Override
    public Map<String, RoleVO> getAllRoleVOMap() {
        return getAllRoleVO().stream().collect(Collectors.toMap(RoleVO::getRoleCode, value -> value));
    }

    @Override
    public Set<String> getAllRoleCodes() {
        return roleMapper.selectList(null).stream()
                .map(RoleDO::getRoleCode)
                .collect(Collectors.toSet());
    }
}
