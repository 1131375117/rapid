package cn.huacloud.taxpreference.sync.es.consumer.impl;

import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import cn.huacloud.taxpreference.services.consumer.entity.ess.OtherDocES;
import cn.huacloud.taxpreference.sync.es.consumer.ElasticsearchConsumer;
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
public class OtherDocConsumer implements ElasticsearchConsumer<OtherDocES> {

    private final ElasticsearchIndexConfig indexConfig;

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public String getIndex() {
        return indexConfig.getOtherDoc().getIndex();
    }

    @Bean
    public Consumer<OtherDocES> saveOtherDoc() {
        return this::save;
    }

    @Bean
    public Consumer<Long> deleteOtherDoc() {
        return this::delete;
    }
}
