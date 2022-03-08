package cn.huacloud.taxpreference.sync.es.consumer.impl;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import cn.huacloud.taxpreference.common.enums.SmsBiz;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import cn.huacloud.taxpreference.services.consumer.SubScribeService;
import cn.huacloud.taxpreference.services.consumer.entity.dos.SubscribeDO;
import cn.huacloud.taxpreference.services.consumer.entity.ess.TaxPreferenceES;
import cn.huacloud.taxpreference.services.message.SmsService;
import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
import cn.huacloud.taxpreference.services.user.mapper.ConsumerUserMapper;
import cn.huacloud.taxpreference.sync.es.consumer.ElasticsearchConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class TaxPreferenceConsumer implements ElasticsearchConsumer<TaxPreferenceES> {

    private final ElasticsearchIndexConfig indexConfig;

    private final SmsService smsService;
    private final SubScribeService subScribeService;
    private final ConsumerUserMapper consumerUserMapper;


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

    @Override
    public void save(TaxPreferenceES source) {
        ElasticsearchConsumer.super.save(source);
        List<SubscribeDO> subscribeDOList = subScribeService.selectList(source.getId(), CollectionType.TAX_PREFERENCE);
        for (SubscribeDO subscribeDO : subscribeDOList) {
            ConsumerUserDO consumerUserDO = consumerUserMapper.selectById(subscribeDO.getConsumerUserId());
            smsService.sendSms(consumerUserDO.getPhoneNumber(), SmsBiz.CONSUMER_SUBSCRIBE__CODE);
        }
    }

    @Bean
    public Consumer<Long> deleteTaxPreference() {
        return this::delete;
    }
}
