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
    @ApiModelProperty("用户修改的字段")
    private String onChangeField;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (!"enterpriseTypes".equals(onChangeField) && !"taxPreferenceItems".equals(onChangeField)) {
            onChangeField = null;
        }
    }
}
