package cn.huacloud.taxpreference.sync.es.consumer.impl;

import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import cn.huacloud.taxpreference.services.consumer.entity.ess.PoliciesExplainES;
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
public class PoliciesExplainConsumer implements ElasticsearchConsumer<PoliciesExplainES> {

    private final ElasticsearchIndexConfig indexConfig;

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public String getIndex() {
        return indexConfig.getPoliciesExplain().getIndex();
    }

    @Bean
    public Consumer<PoliciesExplainES> savePoliciesExplain() {
        return this::save;
    }

    @Bean
    public Consumer<Long> deletePoliciesExplain() {
        return this::delete;
    }
}
