package cn.huacloud.taxpreference.sync.es.consumer.impl;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiMonitorInfoDO;
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
public class MonitorUserApiInfoConsumer implements MySqlConsumer<ApiMonitorInfoDO> {

    @Bean
    public Consumer<ApiMonitorInfoDO> saveUserMonitorInfo() {
        return this::save;
    }

    @Override
    public Logger getLog() {
        return null;
    }

    @Override
    public void save(ApiMonitorInfoDO source) {
        getUserMonitorInfoMapper().insert(source);
    }

}
