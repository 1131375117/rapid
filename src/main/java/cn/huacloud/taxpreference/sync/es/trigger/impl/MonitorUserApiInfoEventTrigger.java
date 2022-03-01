package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.services.common.entity.dos.UserMonitorInfoDO;
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
@Component
public class MonitorUserApiInfoEventTrigger extends MysqlEventTrigger<UserMonitorInfoDO> {

    @Bean
    public Supplier<Flux<UserMonitorInfoDO>> saveUserMonitorInfoSuppler() {
        return saveMany::asFlux;
    }


}
