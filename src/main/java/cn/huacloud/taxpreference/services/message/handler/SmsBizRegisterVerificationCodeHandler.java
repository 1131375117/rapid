package cn.huacloud.taxpreference.services.message.handler;

import cn.huacloud.taxpreference.common.utils.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * 短信注册验证处理器
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class SmsBizRegisterVerificationCodeHandler extends AbstractSmsBizVerificationCodeHandler {

    @Override
    protected Function<String, String> redisKeyGetter() {
        return RedisKeyUtil::getSmsRegisterRedisKey;
    }
}
