package cn.huacloud.taxpreference.services.openapi.message.impl;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiMonitorInfoDO;
import cn.huacloud.taxpreference.services.openapi.message.OpenApiMessageSender;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

/**
 * @author wangkh
 */
@Component
public class OpenApiMessageSenderImpl implements OpenApiMessageSender {

    private final Sinks.Many<ApiMonitorInfoDO> apiMonitorInfoDOMany = Sinks.many().unicast().onBackpressureBuffer();

    @Bean("apiMonitorInfoSupplier")
    public Supplier<Flux<ApiMonitorInfoDO>> apiMonitorInfoSupplier() {
        return apiMonitorInfoDOMany::asFlux;
    }

    @Override
    public void sendApiMonitorInfo(ApiMonitorInfoDO apiMonitorInfoDO) {
        if (apiMonitorInfoDO != null) {
            apiMonitorInfoDOMany.tryEmitNext(apiMonitorInfoDO);
        }
    }
}
