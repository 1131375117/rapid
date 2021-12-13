package cn.huacloud.taxpreference.services.message.interceptor;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.MsgType;
import cn.huacloud.taxpreference.common.enums.SmsBiz;
import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.message.SmsService;
import cn.huacloud.taxpreference.services.message.mapper.MsgRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 短信发送速率控制器
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SmsBizRateLimitInterceptor implements SmsService.Interceptor {

    private final MsgRecordMapper msgRecordMapper;

    private final SysParamService sysParamService;

    private static final long DEFAULT_RATE_LIMIT_SECONDS = 60;

    private static final String SYS_PARAM_TYPE = "sms.rateLimitSeconds";

    @Override
    public void apply(List<String> phoneNumbers, SmsBiz smsBiz) {
        Long rateLimitSeconds = sysParamService.getSingleParamValue(SYS_PARAM_TYPE, null, Long.class, DEFAULT_RATE_LIMIT_SECONDS);
        for (String phoneNumber : phoneNumbers) {
            LocalDateTime lastTime = msgRecordMapper.getLimitLastCreateTime(MsgType.SMS, smsBiz, phoneNumber, rateLimitSeconds);
            if (lastTime != null) {
                long seconds = Duration.between(LocalDateTime.now(), lastTime.plus(rateLimitSeconds, ChronoUnit.SECONDS)).getSeconds();
                if (seconds > 0) {
                    TaxPreferenceException exception = BizCode._4404.exception(seconds);
                    exception.setData(seconds);
                    throw exception;
                }
            }
        }
    }
}
