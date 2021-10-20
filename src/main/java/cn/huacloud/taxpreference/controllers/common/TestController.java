package cn.huacloud.taxpreference.controllers.common;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试用接口
 * @author wangkh
 */
@Api(tags = "测试")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TestController {

    @ApiOperation("接口权限测试")
    @SaCheckPermission("permission_test")
    @GetMapping("/test/permissionTest")
    public ResultVO<Void> permissionTest() {
        return ResultVO.ok();
    }
}
