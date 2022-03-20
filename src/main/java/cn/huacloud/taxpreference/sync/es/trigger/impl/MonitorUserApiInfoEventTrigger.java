package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiMonitorInfoDO;
import cn.huacloud.taxpreference.sync.es.trigger.MysqlEventTrigger;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

/**
 * 监控事件触发器
 *
 * @author fuhua
 */
/*@RequiredArgsConstructor
@Component*/
public class MonitorUserApiInfoEventTrigger extends MysqlEventTrigger<ApiMonitorInfoDO> {

    @Bean
    public Supplier<Flux<ApiMonitorInfoDO>> saveUserMonitorInfoSuppler() {
        return saveMany::asFlux;
    }


}
