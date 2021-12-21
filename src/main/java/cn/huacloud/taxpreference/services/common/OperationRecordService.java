package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;

/**
 * 操作记录服务
 *
 * @author hua-cloud
 */
public interface OperationRecordService {
    /**
     * 操作记录
     * @param operationRecordDTO
     * @param consumerUserId
     */
    void saveOperationRecord(OperationRecordDTO operationRecordDTO, Long consumerUserId);
}
