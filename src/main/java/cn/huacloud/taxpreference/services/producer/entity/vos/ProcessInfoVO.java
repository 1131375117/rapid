package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.enums.process.ProcessStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 流程审批信息
 * @author: fuhua
 * @create: 2021-10-26 10:12
 **/
@Data
public class ProcessInfoVO {
    @ApiModelProperty("申请时间")
    private LocalDateTime createTime;
    @ApiModelProperty("申请人")
    private String creatorName;
    @ApiModelProperty("审批人")
    private String approverName;
    @ApiModelProperty("审批结果")
    private ProcessStatus processStatus;
    @ApiModelProperty("审批说明")
    private String approvalNote;
    @ApiModelProperty("审批时间")
    private LocalDateTime approvalTime;
}
