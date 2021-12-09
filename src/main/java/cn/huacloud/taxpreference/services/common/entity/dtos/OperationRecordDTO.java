package cn.huacloud.taxpreference.services.common.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 操作记录实体
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
public class OperationRecordDTO {

    @ApiModelProperty(notes = "前端不传")
    private Long id;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private String operationType;

    /**
     * 操作参数
     */
    @ApiModelProperty("操作参数")
    private String operationParam;

}
