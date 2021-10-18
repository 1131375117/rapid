package cn.huacloud.taxpreference.controllers.user;

import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户接口
 * @author wangkh
 */
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 查询用户列表
     * 条件：账号（模糊查询）、姓名（模糊查询）、角色（精确）
     */

    /**
     * 新增用户
     * 必填字段：账号、姓名、手机号码、密码
     * 选填字段：身份证号码、邮箱
     */

    /**
     * 修改用户信息
     */

    /**
     * 根据ID查询用户详情
     */

    /**
     * 查询当前用户信息
     */

    /**
     * 根据ID禁用\启用用户
     */

    /**
     * 根据ID删除用户
     */
}
