package cn.huacloud.taxpreference.controllers.sso;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 单点登录接口
 * @author wangkh
 */
@Api(tags = "用户登录")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class SSOController {

    private final UserService userService;

    /**
     * 用户登录接口
     * @param userAccount 用户名称
     * @param password 用户密码
     * @return
     */
    @ApiOperation("用户登录接口")
    @PostMapping("/sso/login")
    public ResultVO<LoginUserVO> login(@RequestParam("userAccount") String userAccount,
                                         @RequestParam("password") String password) {
        // 根据用户名查找用户
        UserDO userDO = userService.getUserDOByUserAccount(userAccount);
        if (userDO == null) {
            throw BizCode._4200.exception();
        }
        // 校验用户密码
        if (!userDO.getPassword().equals(SaSecureUtil.md5(password))) {
            throw BizCode._4200.exception();
        }
        // 执行登录
        StpUtil.login(userDO.getId());
        // 查询用户登录视图
        LoginUserVO loginUserVO = userService.getLoginUserVOById(userDO.getId());
        // 保存用户登录视图到 session
        StpUtil.getSession().set(UserUtil.LOGIN_USER, loginUserVO);
        // 返回结果
        return ResultVO.ok(loginUserVO);
    }

    /**
     * 查询当前用户信息
     */
    @ApiOperation("查询当前用户信息")
    @GetMapping("/sso/currentUser")
    public ResultVO<LoginUserVO> getLoginUserVO() {
        StpUtil.checkLogin();
        return ResultVO.ok(UserUtil.getCurrentUser());
    }

    /**
     * 登出
     * @return
     */
    @ApiOperation("登出")
    @PostMapping("/sso/logout")
    public ResultVO<Void> logout() {
        StpUtil.logout();
        return ResultVO.ok();
    }
}
