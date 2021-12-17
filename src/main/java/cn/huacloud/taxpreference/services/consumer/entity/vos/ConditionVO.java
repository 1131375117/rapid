package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 税收优惠条件视图对象
 * @author wangkh
 */
@Data
public class ConditionVO {
    @ApiModelProperty("条件名称")
    private String conditionName;
    @ApiModelProperty("条件数值")
    private String conditionValue;
}
