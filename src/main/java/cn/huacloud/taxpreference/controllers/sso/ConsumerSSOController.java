package cn.huacloud.taxpreference.controllers.sso;

import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.common.utils.ConsumerStpUtil;
import cn.huacloud.taxpreference.common.utils.ConsumerUerUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.message.SmsService;
import cn.huacloud.taxpreference.services.user.entity.dtos.PasswordLoginDTO;
import cn.huacloud.taxpreference.services.user.entity.dtos.RetrievePasswordDTO;
import cn.huacloud.taxpreference.services.user.entity.dtos.SmsLoginDTO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserRegisterDTO;
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

    private final StringRedisTemplate stringRedisTemplate;

    private final SmsService smsService;

    @ApiOperation("短信验证码登录")
    @PostMapping("/consumer/sso/smsLogin")
    public ResultVO<ProducerLoginUserVO> smsLogin(@RequestBody SmsLoginDTO smsLoginDTO) {

        return ResultVO.ok(null);
    }

    @ApiOperation("密码登录")
    @PostMapping("/consumer/sso/passwordLogin")
    public ResultVO<ProducerLoginUserVO> passwordLogin(@RequestBody PasswordLoginDTO passwordLoginDTO) {

        return ResultVO.ok(null);
    }

    @ApiOperation("用户注册")
    @PostMapping("/consumer/sso/register")
    public ResultVO<ProducerLoginUserVO> register(@RequestBody UserRegisterDTO userRegisterDTO) {

        return ResultVO.ok(null);
    }

    @ApiOperation("找回密码")
    public ResultVO<Void> retrievePassword(@RequestBody RetrievePasswordDTO retrievePasswordDTO) {

        return ResultVO.ok(null);
    }

    @ApiOperation("查询当前用户信息")
    @GetMapping("/consumer/sso/currentUser")
    public ResultVO<ProducerLoginUserVO> getLoginUserVO() {
        ConsumerStpUtil.checkLogin();
        return ResultVO.ok(ConsumerUerUtil.getCurrentUser());
    }

    @ApiOperation("登出")
    @PostMapping("/consumer/sso/logout")
    public ResultVO<Void> logout() {
        String currentUserAccount = ConsumerUerUtil.getCurrentUserAccount();
        StpUtil.logout();
        log.info("用户登出, userAccount: {}", currentUserAccount);
        return ResultVO.ok();
    }
}
