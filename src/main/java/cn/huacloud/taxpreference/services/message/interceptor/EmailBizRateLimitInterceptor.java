package cn.huacloud.taxpreference.services.message.interceptor;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.EmailBiz;
import cn.huacloud.taxpreference.common.enums.MsgType;
import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.message.EmailService;
import cn.huacloud.taxpreference.services.message.mapper.MsgRecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 邮件业务拦截器
 *
 * @author fuhua
 **/
@Slf4j
@RequiredArgsConstructor
@Component
public class EmailBizRateLimitInterceptor implements EmailService.Interceptor {
    private static final long DEFAULT_RATE_LIMIT_SECONDS = 60;
    private final SysParamService sysParamService;
    private final MsgRecordMapper msgRecordMapper;

    @Override
    public void apply(List<String> emails, EmailBiz emailBiz) {
        Long rateLimitSeconds = sysParamService.getSingleParamValue(SysParamTypes.SES_RATE_LIMIT_SECONDS, null, Long.class, DEFAULT_RATE_LIMIT_SECONDS);
        for (String email : emails) {
            LocalDateTime lastTime = msgRecordMapper.getLimitLastCreateTime(MsgType.EMAIL, emailBiz, email, rateLimitSeconds);
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

