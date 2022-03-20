package cn.huacloud.taxpreference.sync.es.consumer.impl;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiStatisticsDO;
import cn.huacloud.taxpreference.sync.es.consumer.MySqlConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * @author fuhua
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class MonitorApiConsumer implements MySqlConsumer<ApiStatisticsDO> {

    @Bean
    public Consumer<ApiStatisticsDO> saveApiUserStatistics() {
        return this::save;
    }

    @Bean
    public Consumer<ApiStatisticsDO> updateApiUserStatistics() {
        return this::update;
    }

    @Override
    public Logger getLog() {
        return null;
    }

    @Override
    public void save(ApiStatisticsDO source) {
        getMonitorService().insert(source);
    }

    @Override
    public void update(ApiStatisticsDO source) {
        getMonitorService().update(source);
    }
}
