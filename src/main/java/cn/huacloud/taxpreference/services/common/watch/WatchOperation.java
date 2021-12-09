package cn.huacloud.taxpreference.services.common.watch;


import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;

/**
 * @author hua-cloud
 */
public interface WatchOperation {

    boolean supported(String operationType);

    void apply(DocType docType, OperationRecordDTO operationRecordDTO);

}
