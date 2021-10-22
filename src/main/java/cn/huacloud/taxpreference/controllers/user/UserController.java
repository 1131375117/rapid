package cn.huacloud.taxpreference.controllers.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserQueryDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserVO;
import cn.huacloud.taxpreference.services.user.entity.vos.UserListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口
 * @author wangkh
 */
@Api(tags = "用户管理")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 查询后台用户列表
     * 条件：账号（模糊查询）、姓名（模糊查询）、角色（精确）
     */
    @PermissionInfo(name = "查询后台用户列表", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_query")
    @ApiOperation("查询后台用户列表")
    @PostMapping("/producer/user/query")
    public ResultVO<PageVO<UserListVO>> producerUserPageQuery(@RequestBody UserQueryDTO userQueryDTO) {
        userQueryDTO.paramReasonable();
        PageVO<UserListVO> pageVO = userService.producerUserPageQuery(userQueryDTO);
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
    public ResultVO<ProducerUserVO> saveProducerUser(@Validated(ValidationGroup.Create.class) ProducerUserVO producerUserVO) {
        userService.saveProducerUser(producerUserVO);
        return ResultVO.ok(producerUserVO);
    }


    /**
     * 修改后台用户
     */
    @PermissionInfo(name = "修改后台用户", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_update")
    @ApiOperation("修改后台用户")
    @PostMapping("/producer/user")
    public ResultVO<ProducerUserVO> updateProducerUser(@Validated(ValidationGroup.Update.class) ProducerUserVO producerUserVO) {
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
    @PermissionInfo(name = "给用户赋予权限", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("producer_user_delete")
    @ApiOperation("给指定ID用户赋予角色")
    @PutMapping("/producer/user/{userId}/role")
    public ResultVO<Void> setRoleToUser(@PathVariable("userId") String userId,
                                        @RequestParam("roleCodes") List<String> roleCodes) {
        userService.setRoleToUser(userId, roleCodes);
        return ResultVO.ok();
    }
}
