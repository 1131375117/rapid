package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;

/**
 * 操作记录服务
 *
 * @author hua-cloud
 */
public interface OperationRecordService {
    /**
     * 操作记录
     * @param operationRecordDO
     * @param currentUser
     */
    void operationRecord(OperationRecordDTO operationRecordDO, ProducerLoginUserVO currentUser);
}
