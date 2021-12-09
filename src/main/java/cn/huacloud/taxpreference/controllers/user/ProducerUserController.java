package cn.huacloud.taxpreference.controllers.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.ValidationUtil;
import cn.huacloud.taxpreference.services.user.ProducerUserService;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserQueryDTO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserRoleAddDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserListVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口
 * @author wangkh
 */
@Api(tags = "后台用户管理")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ProducerUserController {

    private final ProducerUserService userService;

    /**
     * 查询后台用户列表
     * 条件：账号（模糊查询）、姓名（模糊查询）、角色（精确）
     */
    @PermissionInfo(name = "查询后台用户列表", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_query")
    @ApiOperation("查询后台用户列表")
    @PostMapping("/producer/user/query")
    public ResultVO<PageVO<ProducerUserListVO>> producerUserPageQuery(@RequestBody UserQueryDTO userQueryDTO) {
        userQueryDTO.paramReasonable();
        PageVO<ProducerUserListVO> pageVO = userService.producerUserPageQuery(userQueryDTO);
        return ResultVO.ok(pageVO);
    }

    /**
     * 新增后台用户
     * 必填字段：账号、姓名、手机号码、密码
     * 选填字段：身份证号码、邮箱
     */
    @PermissionInfo(name = "新增后台用户", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_save")
    @ApiOperation("新增后台用户")
    @PostMapping("/producer/user")
    public ResultVO<ProducerUserVO> saveProducerUser(@Validated(ValidationGroup.Create.class) @RequestBody ProducerUserVO producerUserVO) {
        boolean userAccountExist = userService.isUserAccountExist(producerUserVO.getUserAccount());
        if (userAccountExist) {
            throw BizCode._4212.exception();
        }
        userService.saveProducerUser(producerUserVO);
        return ResultVO.ok(producerUserVO);
    }

    /**
     * 修改后台用户
     */
    @PermissionInfo(name = "修改后台用户", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_update")
    @ApiOperation("修改后台用户")
    @PutMapping("/producer/user")
    public ResultVO<ProducerUserVO> updateProducerUser(@Validated(ValidationGroup.Update.class) @RequestBody ProducerUserVO producerUserVO) throws MethodArgumentNotValidException {
        if (StringUtils.isNotBlank(producerUserVO.getPassword())) {
            ValidationUtil.validate(producerUserVO, ValidationGroup.Manual.class);
        } else {
            producerUserVO.setPassword(null);
        }
        userService.updateProducerUser(producerUserVO);
        return ResultVO.ok(producerUserVO);
    }

    /**
     * 根据ID查询后台用户详情
     */
    @PermissionInfo(name = "根据ID查询后台用户详情", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_get")
    @ApiOperation("根据ID查询后台用户详情")
    @GetMapping("/producer/user/{userId}")
    public ResultVO<ProducerUserVO> getProducerUser(@PathVariable("userId") Long userId) {
        ProducerUserVO producerUserVO = userService.getProducerUserByUserId(userId);
        return ResultVO.ok(producerUserVO);
    }

    /**
     * 根据ID禁用/启用用户
     */
    @PermissionInfo(name = "禁用/启用用户", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_disable")
    @ApiOperation(value = "根据ID禁用/启用用户", notes = "返回data数据为是否禁用")
    @PutMapping("/producer/user/{userId}/disable")
    public ResultVO<Boolean> switchDisableProducerUser(@PathVariable("userId") Long userId) {
        Boolean disable = userService.switchDisableProducerUser(userId);
        return ResultVO.ok(disable);
    }

    /**
     * 根据ID删除用户
     */
    @PermissionInfo(name = "删除用户", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_delete")
    @ApiOperation("根据ID删除用户")
    @DeleteMapping("/producer/user/{userId}")
    public ResultVO<Void> removeProducerUser(@PathVariable("userId") Long userId) {
        userService.removeProducerUser(userId);
        return ResultVO.ok();
    }

    /**
     * 给指定ID用户赋予角色
     */
    @PermissionInfo(name = "给用户赋予角色", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_set_role")
    @ApiOperation("给指定ID用户赋予角色")
    @PutMapping("/producer/user/{userId}/role")
    public ResultVO<Void> setRoleToUser(@PathVariable("userId") Long userId,
                                        @RequestParam("roleCodes") List<String> roleCodes) {
        userService.setRoleToUser(userId, roleCodes);
        return ResultVO.ok();
    }

    @PermissionInfo(name = "移除角色", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_remove_role")
    @ApiOperation("移除指定用户的指定角色")
    @DeleteMapping("/producer/user/{userId}/role/{roleCode}")
    public ResultVO<Void> removeUserRole(@PathVariable("userId") Long userId,
                                         @PathVariable("roleCode") String roleCode) {
        userService.removeUserRole(userId, roleCode);
        return ResultVO.ok();
    }

    @ApiOperation(value = "检查用户账号是否已存在", notes = "true：已存在，不可用；false：不存在，可用")
    @GetMapping("/producer/user/account/exist")
    public ResultVO<Boolean> isUserAccountExist(String userAccount) {
        boolean exist = userService.isUserAccountExist(userAccount);
        return ResultVO.ok(exist);
    }

    @PermissionInfo(name = "批量为用户添加角色", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_add_role")
    @ApiOperation("批量为用户添加角色")
    @PutMapping("/producer/user/addRole")
    public ResultVO<Void> addRoleToUser(@RequestBody List<UserRoleAddDTO> userRoleAddVOList) {
        userService.addRoleToUser(userRoleAddVOList);
        return ResultVO.ok();
    }
}
