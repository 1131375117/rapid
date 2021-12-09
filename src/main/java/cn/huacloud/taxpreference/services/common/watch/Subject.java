package cn.huacloud.taxpreference.services.common.watch;


import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;

/**
 * @author hua-cloud
 */
public interface Subject {

    boolean supported(DocType docType);

    void apply(DocType docType, OperationRecordDTO operationRecordDTO);

}
