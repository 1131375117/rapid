package cn.huacloud.taxpreference.sync.es.consumer.impl;

import cn.huacloud.taxpreference.services.common.entity.dos.UserMonitorInfoDO;
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
public class MonitorUserApiInfoConsumer implements MySqlConsumer<UserMonitorInfoDO> {

    @Bean
    public Consumer<UserMonitorInfoDO> saveUserMonitorInfo() {
        return this::save;
    }

    @Override
    public Logger getLog() {
        return null;
    }

    @Override
    public void save(UserMonitorInfoDO source) {
        getUserMonitorInfoMapper().insert(source);
    }

}
