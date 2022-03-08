package cn.huacloud.taxpreference.controllers.sso;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.huacloud.taxpreference.common.annotations.ConsumerUserCheckLogin;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.utils.*;
import cn.huacloud.taxpreference.services.user.ConsumerUserService;
import cn.huacloud.taxpreference.services.user.entity.dtos.*;
import cn.huacloud.taxpreference.services.user.entity.vos.ConsumerLoginUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;

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

    private final ConsumerUserService consumerUserService;

    @ApiOperation("短信验证码登录")
    @PostMapping("/consumer/sso/smsLogin")
    public ResultVO<ConsumerLoginUserVO> smsLogin(@RequestBody SmsLoginDTO smsLoginDTO) {
        String phoneNumber = smsLoginDTO.getPhoneNumber();
        // 校验验证码
        checkVerificationCode(RedisKeyUtil.getSmsLoginRedisKey(smsLoginDTO.getPhoneNumber()), smsLoginDTO.getVerificationCode());
        // 查找用户登录信息
        ConsumerLoginUserVO loginUserVO = consumerUserService.getLoginUserVO(phoneNumber);
        if (loginUserVO == null) {
            // 使用手机号自动创建账号
            consumerUserService.autoCreateUserByPhoneNumber(phoneNumber);
            loginUserVO = consumerUserService.getLoginUserVO(phoneNumber);
        }

        // 执行登录
        ConsumerStpUtil.login(loginUserVO.getId());
        // 保存用户登录视图到 session
        ConsumerStpUtil.getSession().set(ConsumerUserUtil.CONSUMER_USER, loginUserVO);
        // 返回结果
        return ResultVO.ok(loginUserVO);
    }

    @ApiOperation("密码登录")
    @PostMapping("/consumer/sso/passwordLogin")
    public ResultVO<ConsumerLoginUserVO> passwordLogin(@RequestBody PasswordLoginDTO passwordLoginDTO) {
        // 校验验证码
        String redisKey = RedisKeyUtil.getCaptchaRedisKey(passwordLoginDTO.getCaptchaId());
        String serverCaptchaCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (serverCaptchaCode == null || !serverCaptchaCode.equalsIgnoreCase(passwordLoginDTO.getCaptchaCode())) {
            throw BizCode._4602.exception();
        }
        stringRedisTemplate.delete(redisKey);

        // 获取用户信息
        ConsumerLoginUserVO loginUserVO = consumerUserService.getLoginUserVOWithPassword(passwordLoginDTO.getAccount());
        if (loginUserVO == null) {
            throw BizCode._4603.exception();
        }
        // 校验用户密码
        if (!loginUserVO.getPassword().equals(SaSecureUtil.md5(PasswordSecureUtil.decrypt(passwordLoginDTO.getPassword())))) {
            throw BizCode._4603.exception();
        }
        // 擦除密码
        loginUserVO.setPassword(null);

        // 执行登录
        ConsumerStpUtil.login(loginUserVO.getId());
        // 保存用户登录视图到 session
        ConsumerStpUtil.getSession().set(ConsumerUserUtil.CONSUMER_USER, loginUserVO);
        // 返回结果
        return ResultVO.ok(loginUserVO);
    }

    @ApiOperation("用户注册")
    @PostMapping("/consumer/sso/register")
    public ResultVO<ConsumerLoginUserVO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        // 校验验证码
        checkVerificationCode(RedisKeyUtil.getSmsRegisterRedisKey(userRegisterDTO.getPhoneNumber()), userRegisterDTO.getVerificationCode());
        // 执行注册
        consumerUserService.manualCreateUser(userRegisterDTO.getPhoneNumber(), PasswordSecureUtil.decrypt(userRegisterDTO.getPassword()));
        return ResultVO.ok(null);
    }

    @ApiOperation("检查账号是否存在")
    @GetMapping("/consumer/sso/checkAccountExist")
    public ResultVO<Boolean> checkAccountExist(String account) {
        ConsumerLoginUserVO loginUserVO = consumerUserService.getLoginUserVO(account);
        return ResultVO.ok(loginUserVO != null);
    }

    @ApiOperation("找回密码")
    @PutMapping("/consumer/sso/retrievePassword")
    public ResultVO<Void> retrievePassword(@RequestBody RetrievePasswordDTO retrievePasswordDTO) {
        // 校验验证码
        checkVerificationCode(RedisKeyUtil.getSmsRetrievePasswordRedisKey(retrievePasswordDTO.getPhoneNumber()), retrievePasswordDTO.getVerificationCode());
        consumerUserService.retrievePassword(retrievePasswordDTO.getPhoneNumber(), PasswordSecureUtil.decrypt(retrievePasswordDTO.getPassword()));
        return ResultVO.ok(null);
    }

    @ApiOperation("查询当前用户信息")
    @GetMapping("/consumer/sso/currentUser")
    public ResultVO<ConsumerLoginUserVO> getLoginUserVO() {
        ConsumerStpUtil.checkLogin();
        return ResultVO.ok(ConsumerUserUtil.getCurrentUser());
    }

    @ApiOperation("登出")
    @PostMapping("/consumer/sso/logout")
    public ResultVO<Void> logout() {
        String currentUserAccount = ConsumerUserUtil.getCurrentUserAccount();
        ConsumerStpUtil.logout();
        log.info("用户登出, userAccount: {}", currentUserAccount);
        return ResultVO.ok();
    }

    /**
     * 校验验证码
     *
     * @param redisKey         redis键
     * @param verificationCode 验证码
     */
    private void checkVerificationCode(String redisKey, String verificationCode) {
        String serverVerificationCode = stringRedisTemplate.opsForValue().get(redisKey);
        if (serverVerificationCode == null || !serverVerificationCode.equals(verificationCode)) {
            throw BizCode._4600.exception();
        }
        stringRedisTemplate.delete(redisKey);
    }

    /**
     * 根据id修改前台用户昵称
     */
    @PermissionInfo(name = "前台用户信息管理", group = PermissionGroup.CONSUMER_MANAGE)
    @ConsumerUserCheckLogin
    @ApiOperation("修改前台用户昵称")
    @PutMapping("/consumer/sso/updateName")
    public ResultVO<Void> producerUserPageQuery(@RequestParam @NotEmpty(message = "用户名不能为空") String username) {
        consumerUserService.updateConsumerName(ConsumerUserUtil.getCurrentUser().getUserAccount(), username);
        // 查找用户登录信息
        ConsumerLoginUserVO loginUserVO = consumerUserService.getLoginUserVO(ConsumerUserUtil.getCurrentUser().getUserAccount());
        ConsumerStpUtil.getSession().set(ConsumerUserUtil.CONSUMER_USER, loginUserVO);
        return ResultVO.ok();
    }

    @ApiOperation("修改密码")
    @PutMapping("/consumer/sso/updatePassword")
    @ConsumerUserCheckLogin
    public ResultVO<Void> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        // 校验验证码
        checkVerificationCode(RedisKeyUtil.getSmsRetrievePasswordRedisKey(updatePasswordDTO.getPhoneNumber()), updatePasswordDTO.getVerificationCode());
        consumerUserService.updatePassword(updatePasswordDTO.getPhoneNumber(), PasswordSecureUtil.decrypt(updatePasswordDTO.getNewPassword()));
        // 查找用户登录信息
        ConsumerLoginUserVO loginUserVO = consumerUserService.getLoginUserVO(ConsumerUserUtil.getCurrentUser().getUserAccount());
        ConsumerStpUtil.logout();
        return ResultVO.ok(null);
    }

    @ApiOperation("邮箱绑定")
    @PutMapping("/consumer/sso/bindEmail")
    @ConsumerUserCheckLogin
    public ResultVO<Void> bindEmail(@RequestBody UserEmailBindDTO userEmailBindDTO) {
        // 校验验证码
        checkVerificationCode(RedisKeyUtil.getEmailBindRedisKey(userEmailBindDTO.getEmail()), userEmailBindDTO.getVerificationCode());
        consumerUserService.bindEmail(userEmailBindDTO.getEmail(), ConsumerUserUtil.getCurrentUser().getUserAccount());
        // 查找用户登录信息
        ConsumerLoginUserVO loginUserVO = consumerUserService.getLoginUserVO(ConsumerUserUtil.getCurrentUser().getUserAccount());
        ConsumerStpUtil.getSession().set(ConsumerUserUtil.CONSUMER_USER, loginUserVO);
        return ResultVO.ok(null);
    }
}
