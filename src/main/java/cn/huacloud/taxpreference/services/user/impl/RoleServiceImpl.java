package cn.huacloud.taxpreference.services.user.impl;

import cn.huacloud.taxpreference.common.constants.UserConstants;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.user.PermissionService;
import cn.huacloud.taxpreference.services.user.RoleService;
import cn.huacloud.taxpreference.services.user.entity.dos.RoleDO;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleListVO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleVO;
import cn.huacloud.taxpreference.services.user.mapper.RoleMapper;
import cn.huacloud.taxpreference.services.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    private final UserMapper userMapper;

    private final PermissionService permissionService;

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

    @Override
    public List<RoleDO> getRoleDOByRoleCodes(Collection<String> roleCodes) {
        if (roleCodes.isEmpty()) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<RoleDO> queryWrapper = Wrappers.lambdaQuery(RoleDO.class)
                .in(RoleDO::getRoleCode, roleCodes);
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public PageVO<RoleListVO> rolePageQuery(PageQueryDTO pageQueryDTO) {
        IPage<RoleListVO> pageVO = roleMapper.rolePageQuery(pageQueryDTO.createQueryPage());
        // 处理权限码值
        for (RoleListVO record : pageVO.getRecords()) {
            String permissionCodes = record.getPermissionCodes();
            if (StringUtils.isBlank(permissionCodes)) {
                record.setPermissionCodeList(new ArrayList<>());
                continue;
            }
            record.setPermissionCodeList(Arrays.asList(permissionCodes.split(",")));
        }
        return PageVO.createPageVO(pageVO);
    }

    @Transactional
    @Override
    public void saveRole(RoleVO roleVO) {
        // 数据校验
        if (roleMapper.isRoleCodeExist(roleVO.getRoleCode())) {
            throw BizCode._4206.exception();
        }
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleVO, roleDO);
        roleDO.setRoleCode(roleVO.getRoleCode().toUpperCase());
        roleMapper.insert(roleDO);
        roleVO.setId(roleDO.getId());
        log.info("添加角色成功，roleDO：{}", roleDO);
    }

    @Transactional
    @Override
    public void updateRole(RoleVO roleVO) {
        RoleDO roleDO = roleMapper.selectById(roleVO.getId());
        // 数据校验
        if (roleDO == null) {
            throw BizCode._4100.exception();
        }
        // 设置属性
        roleDO.setRoleName(roleVO.getRoleName());
        roleDO.setNote(roleVO.getNote());
        // 执行更新
        roleMapper.updateById(roleDO);
        log.info("修改角色信息成功，roleDO：{}", roleDO);
    }

    @Override
    public void deleteRole(String roleId) {
        RoleDO roleDO = roleMapper.selectById(roleId);
        if (roleDO == null) {
            return;
        }

        // 检查是否为管理员角色
        checkAdminRole(roleDO.getRoleCode());

        // 参数校验，只能删除未被使用的角色
        List<UserDO> userDOS = userMapper.getUserDOByRoleCode(roleDO.getRoleCode());
        if (userDOS.size() > 0) {
            throw BizCode._4207.exception();
        }

        // 执行删除
        roleMapper.deleteById(roleId);
        log.info("删除角色信息成功，roleDO：{}", roleDO);
    }

    @Override
    public void setPermissionToRole(Long roleId, List<String> permissionCodes) {
        // 参数校验
        RoleDO roleDO = roleMapper.selectById(roleId);
        if (roleDO == null) {
            throw BizCode._4100.exception();
        }

        // 检查是否为管理员角色
        checkAdminRole(roleDO.getRoleCode());

        Set<String> allPermissionCodeSet = permissionService.getAllPermissionCodeSet();

        Set<String> permissionCodeSet = new TreeSet<>(permissionCodes);
        // 去除权限库不存在的权限码值
        permissionCodeSet.retainAll(allPermissionCodeSet);

        String permissionCodesStr = String.join(",", permissionCodeSet);

        roleDO.setPermissionCodes(permissionCodesStr);

        // 执行更新
        roleMapper.updateById(roleDO);
        log.info("设置角色权限成功，roleDO：{}", roleDO);
    }

    /**
     * 检查是否为管理员角色
     * @param roleCode 角色码值
     */
    private void checkAdminRole(String roleCode) {
        if (UserConstants.ADMIN_ROLE_CODE.equalsIgnoreCase(roleCode)) {
            throw BizCode._4208.exception();
        }
    }
}
