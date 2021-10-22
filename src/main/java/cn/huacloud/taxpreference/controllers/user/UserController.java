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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @SaCheckPermission("producer_user_add")
    @ApiOperation("新增后台用户")
    @PostMapping("/producer/user")
    public ResultVO<ProducerUserVO> saveProducerUser(@Validated(ValidationGroup.Create.class) ProducerUserVO producerUserVO) {
        userService.saveProducerUser(producerUserVO);
        return ResultVO.ok(producerUserVO);
    }


    /**
     * 修改用户信息
     */

    /**
     * 根据ID查询用户详情
     */

    /**
     * 根据ID禁用\启用用户
     */

    /**
     * 根据ID删除用户
     */
}
