package cn.huacloud.taxpreference.controllers.sso;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.ProducerUserUtil;
import cn.huacloud.taxpreference.common.utils.RedisKeyUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.user.ProducerUserService;
import cn.huacloud.taxpreference.services.user.entity.dos.ProducerUserDO;
import cn.huacloud.taxpreference.services.user.entity.vos.CaptchaVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 单点登录接口
 *
 * @author wangkh
 */
@Slf4j
@Api(tags = "后台用户登录")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ProducerSSOController {

    private final ProducerUserService userService;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 用户登录接口
     *
     * @param userAccount 用户名称
     * @param password    用户密码
     */
    @ApiOperation("用户登录接口")
    @PostMapping("/producer/sso/login")
    public ResultVO<ProducerLoginUserVO> login(@RequestParam("userAccount") String userAccount,
                                               @RequestParam("password") String password,
                                               @RequestParam(name = "captchaId", defaultValue = "2c54df8c-5f8c-489b-bf3c-84bd14d1d669") String captchaId,
                                               @RequestParam(name = "captchaCode", defaultValue = "1234") String captchaCode) {
        Environment environment = SpringUtil.getBean(Environment.class);
        boolean isDev = Arrays.asList(environment.getActiveProfiles()).contains("dev");

        // 校验验证码
        if (!isDev) {
            String captchaRedisKey = RedisKeyUtil.getCaptchaRedisKey(captchaId);
            String serverCaptchaCode = stringRedisTemplate.opsForValue().get(captchaRedisKey);
            if (serverCaptchaCode == null || !serverCaptchaCode.equalsIgnoreCase(captchaCode)) {
                throw BizCode._4210.exception();
            }
            stringRedisTemplate.delete(captchaRedisKey);
        }

        // 根据用户名查找用户
        ProducerUserDO producerUserDO = userService.getUserDOByUserAccount(userAccount);
        if (producerUserDO == null) {
            log.info("用户登录, 用户账户不存在, userAccount: {}", userAccount);
            throw BizCode._4204.exception();
        }
        // 校验用户密码
        if (!producerUserDO.getPassword().equals(SaSecureUtil.md5(password))) {
            log.info("用户登录, 密码不正, userAccount: {}", userAccount);
            throw BizCode._4204.exception();
        }

        // 检查账号禁用状态
        if (producerUserDO.getDisable()) {
            throw BizCode._4205.exception();
        }

        // 执行登录
        StpUtil.login(producerUserDO.getId());
        // 查询用户登录视图
        ProducerLoginUserVO loginUserVO = userService.getLoginUserVOById(producerUserDO.getId());
        // 保存用户登录视图到 session
        StpUtil.getSession().set(ProducerUserUtil.PRODUCER_USER, loginUserVO);
        log.info("用户登录, 用户登录成功, userAccount: {}", userAccount);
        // 返回结果
        return ResultVO.ok(loginUserVO);
    }

    /**
     * 查询当前用户信息
     */
    @ApiOperation("查询当前用户信息")
    @GetMapping("/producer/sso/currentUser")
    public ResultVO<ProducerLoginUserVO> getLoginUserVO() {
        StpUtil.checkLogin();
        return ResultVO.ok(ProducerUserUtil.getCurrentUser());
    }

    /**
     * 登出
     */
    @ApiOperation("登出")
    @PostMapping("/producer/sso/logout")
    public ResultVO<Void> logout() {
        String currentUserAccount = ProducerUserUtil.getCurrentUserAccount();
        StpUtil.logout();
        log.info("用户登出, userAccount: {}", currentUserAccount);
        return ResultVO.ok();
    }

    @ApiOperation("获取图片验证码")
    @GetMapping("/sso/captcha")
    public ResultVO<CaptchaVO> getCaptcha(@RequestParam(name = "width", defaultValue = "600") Integer width,
                                          @RequestParam(name = "height", defaultValue = "300") Integer height) {
        if (width > 1200) {
            width = 1200;
        }
        if (height > 600) {
            height = 600;
        }

        // 生成图片验证码
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(width, height, 4, 200);
        // 属性设置
        CaptchaVO captchaVO = new CaptchaVO();
        String uuid = UUID.randomUUID().toString();
        captchaVO.setCaptchaId(uuid);
        captchaVO.setImageBase64(lineCaptcha.getImageBase64());

        // 把验证码存入redis
        String captchaRedisKey = RedisKeyUtil.getCaptchaRedisKey(uuid);
        stringRedisTemplate.opsForValue().set(captchaRedisKey, lineCaptcha.getCode());
        // 设置30分钟后过期
        stringRedisTemplate.expire(captchaRedisKey, 30, TimeUnit.MINUTES);

        log.info("获取图片验证成功");
        return ResultVO.ok(captchaVO);
    }


}
