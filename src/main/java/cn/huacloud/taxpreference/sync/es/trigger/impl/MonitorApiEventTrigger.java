package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiStatisticsDO;
import cn.huacloud.taxpreference.sync.es.trigger.MysqlEventTrigger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

/**
 * 监控事件触发器
 *
 * @author fuhua
 */
@RequiredArgsConstructor
//@Component
public class MonitorApiEventTrigger extends MysqlEventTrigger<ApiStatisticsDO> {

    @Bean
    public Supplier<Flux<ApiStatisticsDO>> saveApiUserStatisticsSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<ApiStatisticsDO>> updateApiUserStatisticsSuppler() {
        return updateMany::asFlux;
    }

}
