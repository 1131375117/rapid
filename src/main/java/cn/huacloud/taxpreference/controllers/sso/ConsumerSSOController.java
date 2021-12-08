package cn.huacloud.taxpreference.controllers.sso;

import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.common.utils.ProducerUserUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.user.ProducerUserService;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 单点登录接口
 *
 * @author wangkh
 */
@Slf4j
@Api(tags = "前台用户登录")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ConsumerSSOController {

    private final ProducerUserService userService;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录接口
     *
     * @param userAccount 用户名称
     * @param password    用户密码
     */
    @ApiOperation("用户登录接口")
    @PostMapping("/consumer/sso/login")
    public ResultVO<ProducerLoginUserVO> login(@RequestParam("userAccount") String userAccount,
                                               @RequestParam("password") String password,
                                               @RequestParam(name = "captchaId", defaultValue = "2c54df8c-5f8c-489b-bf3c-84bd14d1d669") String captchaId,
                                               @RequestParam(name = "captchaCode", defaultValue = "1234") String captchaCode) {

        return ResultVO.ok(null);
    }

    /**
     * 查询当前用户信息
     */
    @ApiOperation("查询当前用户信息")
    @GetMapping("/consumer/sso/currentUser")
    public ResultVO<ProducerLoginUserVO> getLoginUserVO() {
        StpUtil.checkLogin();
        return ResultVO.ok(ProducerUserUtil.getCurrentUser());
    }

    /**
     * 登出
     */
    @ApiOperation("登出")
    @PostMapping("/consumer/sso/logout")
    public ResultVO<Void> logout() {
        String currentUserAccount = ProducerUserUtil.getCurrentUserAccount();
        StpUtil.logout();
        log.info("用户登出, userAccount: {}", currentUserAccount);
        return ResultVO.ok();
    }
}
