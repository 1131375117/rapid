package cn.huacloud.taxpreference.services.common.watch;


import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;

import java.util.HashMap;

/**
 * @author hua-cloud
 */
public interface WatcherOperation {
    HashMap<DocType, EventTrigger<Long, Object>> TYPE_TRIGGER_MAP = new HashMap<>();

    boolean supported(String operationType);

    void apply(DocType docType, OperationRecordDTO operationRecordDTO);

}
