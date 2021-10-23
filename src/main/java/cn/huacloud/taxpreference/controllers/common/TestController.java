package cn.huacloud.taxpreference.controllers.common;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private final UserService userService;

    @Autowired
    private final ObjectMapper objectMapper;

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

    @ApiOperation("新增参数校验测试")
    @PostMapping("/test/validation/student")
    public ResultVO<Student> createValidationTest(@Validated(ValidationGroup.Create.class) @RequestBody Student student) {
        return ResultVO.ok(student);
    }

    @ApiOperation("修改参数校验测试")
    @PutMapping("/test/validation/student")
    public ResultVO<Student> updateValidationTest(@Validated(ValidationGroup.Update.class) @RequestBody Student student) {
        return ResultVO.ok(student);
    }

    /**
     * 此种方式校验不生效，暂不使用
     * @param name
     * @param age
     * @return
     */
    @ApiOperation("单独参数校验测试")
    @PostMapping("/test/validation/param")
    public ResultVO<Void> singleValidationTest(@Validated @NotEmpty(message = "姓名不能为空") @RequestParam("name") String name,
                                               @Validated @Min(value = 1, message = "年龄不能小于1") @RequestParam("age") Integer age) {
        return ResultVO.ok();
    }

    @Data
    public static class Student {
        @NotEmpty(message = "姓名不能为空", groups = ValidationGroup.Create.class)
        private String name;
        @Min(value = 1, message = "年龄不能小于1", groups = ValidationGroup.Update.class)
        private Integer age;

        private LocalDate birthday;

        private LocalDateTime createTime;
    }
}
