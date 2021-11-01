package cn.huacloud.taxpreference.controllers.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.user.PermissionService;
import cn.huacloud.taxpreference.services.user.RoleService;
import cn.huacloud.taxpreference.services.user.entity.vos.PermissionGroupVO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleListVO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户角色接口
 * @author wangkh
 */
@Api(tags = "角色管理")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class RoleController {

    private final RoleService roleService;

    private final PermissionService permissionService;

    /**
     * 角色分页列表
     */
    @PermissionInfo(name = "角色分页列表", group = PermissionGroup.ROLE_MANAGE)
    @SaCheckPermission("role_query")
    @ApiOperation("角色分页列表")
    @PostMapping("/role/query")
    public ResultVO<PageVO<RoleListVO>> rolePageQuery(@RequestBody PageQueryDTO pageQueryDTO) {
        pageQueryDTO.paramReasonable();
        PageVO<RoleListVO> pageVO = roleService.rolePageQuery(pageQueryDTO);
        return ResultVO.ok(pageVO);
    }

    /**
     * 添加角色
     */
    @PermissionInfo(name = "添加角色", group = PermissionGroup.ROLE_MANAGE)
    @SaCheckPermission("role_save")
    @ApiOperation("添加角色")
    @PostMapping("/role")
    public ResultVO<RoleVO> saveRole(@Validated(ValidationGroup.Create.class) @RequestBody RoleVO roleVO) {
        roleService.saveRole(roleVO);
        return ResultVO.ok(roleVO);
    }

    /**
     * 修改角色信息
     */
    @PermissionInfo(name = "修改角色信息", group = PermissionGroup.ROLE_MANAGE)
    @SaCheckPermission("role_update")
    @ApiOperation("修改角色信息")
    @PutMapping("/role")
    public ResultVO<RoleVO> updateRole(@Validated(ValidationGroup.Create.class)@RequestBody RoleVO roleVO) {
        roleService.updateRole(roleVO);
        return ResultVO.ok(roleVO);
    }

    @PermissionInfo(name = "删除角色", group = PermissionGroup.ROLE_MANAGE)
    @SaCheckPermission("role_delete")
    @ApiOperation("删除角色")
    @DeleteMapping("/role/{roleId}")
    public ResultVO<Void> deleteRole(@PathVariable("roleId") String roleId) {
        roleService.deleteRole(roleId);
        return ResultVO.ok();
    }

    @PermissionInfo(name = "给角色赋予权限", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("role_set_permission")
    @ApiOperation("给角色赋予权限")
    @PutMapping("/role/{roleId}/permission")
    public ResultVO<Void> setPermissionToRole(@PathVariable("roleId") Long roleId,
                                        @RequestParam("permissionCode") List<String> permissionCodes) {
        roleService.setPermissionToRole(roleId, permissionCodes);
        return ResultVO.ok();
    }

    @PermissionInfo(name = "获取权限分组列表", group = PermissionGroup.ROLE_MANAGE)
    @SaCheckPermission("permission_group")
    @ApiOperation("获取权限分组列表")
    @GetMapping("/permission/group")
    public ResultVO<List<PermissionGroupVO>> getPermissionGroupVOList() {
        List<PermissionGroupVO> groupList = permissionService.getPermissionGroupVOList();
        return ResultVO.ok(groupList);
    }
}
