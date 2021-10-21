package cn.huacloud.taxpreference.controllers.common;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用接口
 *
 * @author wangkh
 */
@Api(tags = "测试")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TestController {
    @Autowired
    private UserService userService;

    @ApiOperation("接口权限测试")
    @PermissionInfo(name = "接口权限测试", group = PermissionGroup.USER_MANAGE)
    @SaCheckPermission("permission_test")
    @GetMapping("/test/permissionTest")
    public ResultVO<Void> permissionTest() {
        return ResultVO.ok();
    }

    @ApiOperation("获取用户接口测试")
    @PostMapping("/test/user_test")
    public ResultVO<UserDO> insertSysCodeTest() {
        UserDO userDO = userService.getUserDOByUserAccount("admin");
        return ResultVO.ok(userDO);
    }
}
