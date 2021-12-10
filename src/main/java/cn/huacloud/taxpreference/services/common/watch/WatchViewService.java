package cn.huacloud.taxpreference.services.common.watch;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class WatchViewService implements WatchOperation, ApplicationContextAware {

    @Override
    public boolean supported(String operationType) {
        return "views".equals(StringUtils.substringBefore(operationType,"."));
    }

    @Override
    public void apply(DocType docType, OperationRecordDTO operationRecordDTO) {
        if (supported(operationRecordDTO.getOperationType())) {
            TYPE_TRIGGER_MAP.get(docType).saveEvent(Long.valueOf(operationRecordDTO.getOperationParam()));
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(EventTrigger.class).values().forEach(eventTrigger ->
                TYPE_TRIGGER_MAP.put(eventTrigger.docType(), eventTrigger));
    }
}
