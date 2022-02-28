/*
package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.services.common.entity.dos.ApiUserStatisticsDO;
import cn.huacloud.taxpreference.sync.es.trigger.MysqlEventTrigger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

*/
/**
 * 监控事件触发器
 *
 * @author fuhua
 *//*

@RequiredArgsConstructor
@Component
public class MonitorApiEventTrigger extends MysqlEventTrigger<ApiUserStatisticsDO> {

    @Bean
    public Supplier<Flux<ApiUserStatisticsDO>> saveApiUserStatisticsSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<ApiUserStatisticsDO>> updateApiUserStatisticsSuppler() {
        return updateMany::asFlux;
    }

}
*/
