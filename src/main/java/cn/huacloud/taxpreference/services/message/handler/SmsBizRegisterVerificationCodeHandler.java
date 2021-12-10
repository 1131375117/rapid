package cn.huacloud.taxpreference.services.message.handler;

import cn.huacloud.taxpreference.common.utils.MessageUtil;
import cn.huacloud.taxpreference.common.utils.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 短信注册验证处理器
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class SmsBizRegisterVerificationCodeHandler implements SmsBizHandler {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<String> getParams(List<String> phoneNumbers) {

        String phoneNumber = phoneNumbers.get(0);
        // 获取redis键
        String redisKey = RedisKeyUtil.getSmsRegisterRedisKey(phoneNumber);
        // 获取随机验证码
        String verificationCode = MessageUtil.getRandomVerificationCode();
        // 保存到redis
        stringRedisTemplate.opsForValue().set(redisKey, verificationCode, 15L, TimeUnit.MINUTES);

        return Collections.singletonList(verificationCode);
    }
}
