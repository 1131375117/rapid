package cn.huacloud.taxpreference.services.producer.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description: 流程管理提交DTO
 * @author: fuhua
 * @create: 2021-10-25 14:35
 **/
@Data
public class ProcessSubmitDTO {
    @NotNull(message = "流程审批id不能为空")
    @ApiModelProperty("流程审批id")
    private Long id;
    @ApiModelProperty("审批说明")
    private String approvalNote;
    @ApiModelProperty("审核状态")
    @NotEmpty(message = "审核状态不能为空,NOT_APPROVED-待通过，APPROVED-已通过，RETURNED-已退回")
    private String taxPreferenceStatus;

}
