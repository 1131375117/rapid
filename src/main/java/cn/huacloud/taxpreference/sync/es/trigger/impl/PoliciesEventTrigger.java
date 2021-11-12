package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.services.consumer.entity.ess.PoliciesES;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class PoliciesEventTrigger extends EventTrigger<Long, PoliciesES> {

    private final PoliciesMapper policiesMapper;

    @Override
    protected Function<Long, PoliciesES> queryFunction() {
        return policiesMapper::queryES;
    }

    @Bean
    public Supplier<Flux<PoliciesES>> savePoliciesSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<Long>> deletePoliciesSuppler() {
        return deleteMany::asFlux;
    }
}
