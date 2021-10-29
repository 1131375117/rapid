package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class QueryAbolishDTO {
    @ApiModelProperty("政策法规id")
    private Long id;

    @ApiModelProperty("废止状态")
    private String policiesStatus;

    @ApiModelProperty("废止说明")
    private String abolishNote;

    @ApiModelProperty("有效性")
    private String validity;

    @ApiModelProperty("税收优惠的id集合")
    private List<Long> ids;

}
