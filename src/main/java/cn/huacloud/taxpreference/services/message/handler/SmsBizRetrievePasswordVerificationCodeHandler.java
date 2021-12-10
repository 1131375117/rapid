package cn.huacloud.taxpreference.services.message.handler;

import cn.huacloud.taxpreference.common.utils.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * 短信找回密码验证码处理器
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class SmsBizRetrievePasswordVerificationCodeHandler extends AbstractSmsBizVerificationCodeHandler {

    @Override
    protected Function<String, String> redisKeyGetter() {
        return RedisKeyUtil::getSmsRetrievePasswordRedisKey;
    }
}
