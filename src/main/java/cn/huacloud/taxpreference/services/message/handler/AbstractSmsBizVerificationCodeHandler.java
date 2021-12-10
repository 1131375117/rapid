package cn.huacloud.taxpreference.services.message.handler;

import cn.huacloud.taxpreference.common.utils.MessageUtil;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
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
        // 保存到redis
       stringRedisTemplate.opsForValue().set(redisKey, verificationCode, 15L, TimeUnit.MINUTES);

        return Collections.singletonList(verificationCode);
    }

    protected abstract Function<String, String> redisKeyGetter();
}
