package cn.huacloud.taxpreference.common.utils;

import cn.huacloud.taxpreference.config.limit.ConfigLimitDto;

/**
 * Redis键工具类，集中管理Redis键
 * @author wangkh
 */
public class RedisKeyUtil {

    /**
     * 根据captchaId获取验证码redisKey
     * @param captchaId 验证码ID
     * @return redisKey
     */
    public static String getCaptchaRedisKey(String captchaId) {
        return "captcha:" + captchaId;
    }

    /**
     * 获取短信登录验证码redisKey
     * @param phoneNumber 电话号码
     * @return redisKey
     */
    public static String getSmsLoginRedisKey(String phoneNumber) {
        return "sms:login:" + phoneNumber;
    }

    /**
     * 获取短信注册验证码redisKey
     * @param phoneNumber 电话号码
     * @return redisKey
     */
    public static String getSmsRegisterRedisKey(String phoneNumber) {
        return "sms:register:" + phoneNumber;
    }

    /**
     * 获取短信找回密码验证码redisKey
     * @param phoneNumber 电话号码
     * @return redisKey
     */
    public static String getSmsRetrievePasswordRedisKey(String phoneNumber) {
        return "sms:retrievePassword:" + phoneNumber;
    }
    /**
     * 获取限流获取redisKey
     * @param userId 用户id
     * @return redisKey
     */
    public static String getLimitUserRedisKey(String userId, ConfigLimitDto routeConfig) {
        return "api:lmt:"+userId+":"+routeConfig.getIp()+":"+routeConfig.getRequestMethod()+":"+routeConfig.getPath();
    }

}
