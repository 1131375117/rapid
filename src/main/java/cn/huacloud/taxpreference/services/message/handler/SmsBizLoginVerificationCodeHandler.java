package cn.huacloud.taxpreference.services.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class SmsBizLoginVerificationCodeHandler implements SmsBizHandler {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<String> getParams(String phoneNumber) {
        // 获取随机验证码

        // 保存到redis
        return null;
    }
}
