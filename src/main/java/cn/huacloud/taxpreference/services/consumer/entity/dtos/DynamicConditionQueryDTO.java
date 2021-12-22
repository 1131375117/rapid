package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
@Getter
@Setter
public class DynamicConditionQueryDTO extends TaxPreferenceSearchQueryDTO {
    @ApiModelProperty("是否为减免事项修改")
    private Boolean taxPreferenceItemChange;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (taxPreferenceItemChange == null) {
            taxPreferenceItemChange = false;
        }
    }
}
