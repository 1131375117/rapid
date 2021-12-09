package cn.huacloud.taxpreference.services.common.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * 操作记录实体
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
public class OperationRecordDTO {

    @ApiModelProperty(hidden = true )
    private Long id;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    @NotEmpty(message = "操作类型不能为空")
    private String operationType;

    /**
     * 操作参数
     */
    @ApiModelProperty("操作参数")
    private String operationParam;

}
