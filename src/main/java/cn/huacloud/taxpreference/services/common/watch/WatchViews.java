package cn.huacloud.taxpreference.services.common.watch;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class WatchViews implements WatchOperation, ApplicationContextAware {
    private final HashMap<DocType, EventTrigger<Long, Object>> sourceMap = new HashMap<>();

    @Override
    public boolean supported(DocType docType) {
        return sourceMap.containsKey(docType);
    }

    @Override
    public void apply(DocType docType, OperationRecordDTO operationRecordDTO) {
        if (supported(docType)) {
            sourceMap.get(docType).saveEvent(Long.valueOf(operationRecordDTO.getOperationParam()));
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(EventTrigger.class).values().forEach(eventTrigger ->
                sourceMap.put(eventTrigger.docType(), eventTrigger));
    }
}
