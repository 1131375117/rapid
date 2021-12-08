package cn.huacloud.taxpreference.controllers.sso;

import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.common.utils.ConsumerStpUtil;
import cn.huacloud.taxpreference.common.utils.ConsumerUerUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录接口
     */
    @ApiOperation("用户登录接口")
    @PostMapping("/consumer/sso/login")
    public ResultVO<ProducerLoginUserVO> login() {

        return ResultVO.ok(null);
    }

    /**
     * 查询当前用户信息
     */
    @ApiOperation("查询当前用户信息")
    @GetMapping("/consumer/sso/currentUser")
    public ResultVO<ProducerLoginUserVO> getLoginUserVO() {
        ConsumerStpUtil.checkLogin();
        return ResultVO.ok(ConsumerUerUtil.getCurrentUser());
    }

    /**
     * 登出
     */
    @ApiOperation("登出")
    @PostMapping("/consumer/sso/logout")
    public ResultVO<Void> logout() {
        String currentUserAccount = ConsumerUerUtil.getCurrentUserAccount();
        StpUtil.logout();
        log.info("用户登出, userAccount: {}", currentUserAccount);
        return ResultVO.ok();
    }
}
