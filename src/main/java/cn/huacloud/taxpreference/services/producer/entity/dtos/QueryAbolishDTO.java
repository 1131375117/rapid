package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class QueryAbolishDTO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("废止说明")
    private String abolishNote;

    @ApiModelProperty("优惠事项名称")
    private String taxPreferenceName;

    @ApiModelProperty("有效性")
    private String validity;

}
