package cn.huacloud.taxpreference.sync.es.consumer.impl;

import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import cn.huacloud.taxpreference.services.consumer.entity.ess.TaxPreferenceES;
import cn.huacloud.taxpreference.sync.es.consumer.ElasticsearchConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class TaxPreferenceConsumer implements ElasticsearchConsumer<TaxPreferenceES> {

    private final ElasticsearchIndexConfig indexConfig;

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public String getIndex() {
        return indexConfig.getTaxPreference().getIndex();
    }

    @Bean
    public Consumer<TaxPreferenceES> saveTaxPreference() {
        return this::save;
    }

    @Bean
    public Consumer<Long> deleteTaxPreference() {
        return this::delete;
    }
}
