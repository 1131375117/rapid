package cn.huacloud.taxpreference.services.message.handler;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.utils.MessageUtil;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.common.SysParamService;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author wangkh
 */
public abstract class AbstractSmsBizVerificationCodeHandler implements SmsBizHandler {

    @Override
    public List<String> getParams(List<String> phoneNumbers) {

        StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);

        String phoneNumber = phoneNumbers.get(0);
        // 获取redis键
        String redisKey = redisKeyGetter().apply(phoneNumber);
        // 获取随机验证码
        String verificationCode = MessageUtil.getRandomVerificationCode();
        // 获取验证码过期时间
        SysParamService sysParamService = SpringUtil.getBean(SysParamService.class);
        Long expireMinutes = sysParamService.getSingleParamValue(SysParamTypes.SMS_VERIFICATION_CODE_EXPIRE_MINUTES, null, Long.class, 15L);
        // 保存到redis
        stringRedisTemplate.opsForValue().set(redisKey, verificationCode, expireMinutes, TimeUnit.MINUTES);

        return Collections.singletonList(verificationCode);
    }

    protected abstract Function<String, String> redisKeyGetter();
}
