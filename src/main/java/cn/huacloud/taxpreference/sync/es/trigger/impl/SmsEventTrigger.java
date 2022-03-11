package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.taxpreference.TaxPreferenceStatus;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.consumer.entity.ess.TaxPreferenceES;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

/**
 * 税收优惠ES数据事件触发器
 *
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class SmsEventTrigger extends EventTrigger<Long, TaxPreferenceES> {

    private final TaxPreferenceMapper taxPreferenceMapper;

    @Bean
    public Supplier<Flux<TaxPreferenceES>> sendSmsSuppler() {
        return saveMany::asFlux;
    }

    @Override
    public DocType docType() {
        return DocType.TAX_PREFERENCE;
    }

    @Override
    protected TaxPreferenceES getEntityById(Long id) {
        TaxPreferenceDO taxPreferenceDO = taxPreferenceMapper.selectById(id);
        if (taxPreferenceDO == null || taxPreferenceDO.getDeleted() || taxPreferenceDO.getTaxPreferenceStatus() != TaxPreferenceStatus.RELEASED) {
            return null;
        }
        // 属性拷贝
        TaxPreferenceES taxPreferenceES = CustomBeanUtil.copyProperties(taxPreferenceDO, TaxPreferenceES.class);
        return taxPreferenceES;
    }

    @Override
    protected IPage<Long> pageIdList(int pageNum, int pageSize) {

        return null;
    }
}
