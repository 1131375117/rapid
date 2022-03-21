package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.services.common.entity.dtos.OperationRecordDTO;
import cn.huacloud.taxpreference.services.common.entity.dtos.ViewQueryDTO;
import cn.huacloud.taxpreference.services.common.entity.vos.OperationRecordVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PageByOperationVO;

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

    /**
     * 查询操作记录
     * @param pageQueryDTO 浏览查询条件dto
     * @param consumerUserId 用户id
     * @return 操作记录集合
     */
    PageByOperationVO<OperationRecordVO> queryOperationRecord(ViewQueryDTO pageQueryDTO, Long consumerUserId);
}
