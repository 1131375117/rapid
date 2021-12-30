package cn.huacloud.taxpreference.services.common.watch;


import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;

import java.util.HashMap;

/**
 * @author fuhua
 */
public interface WatcherOperation {
    HashMap<DocType, EventTrigger<Long, Object>> TYPE_TRIGGER_MAP = new HashMap<>();

    /**
     * 操作类型判断
     *
     * @param operationType 操作类型
     * @return 根据操作类型返回具体布尔值
     */
    boolean supported(String operationType);

    /**
     * 根据操作类型调用相应的方法保存es
     *
     * @param docType            文档类型
     * @param operationRecordDTO 操作记录传入对象
     */
    void apply(DocType docType, OperationRecordDTO operationRecordDTO);

}
