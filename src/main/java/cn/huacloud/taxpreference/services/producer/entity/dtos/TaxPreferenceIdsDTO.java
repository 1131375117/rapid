package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 税收优惠dto对象
 * @author: fuhua
 * @create: 2021-10-21 09:32
 **/
@Data
public class TaxPreferenceIdsDTO {
    /**
     * id
     */
    @ApiModelProperty("id")
    @NotNull(message = "id不能为空", groups = ValidationGroup.Update.class)
    @Min(value = 1, message = "id必须为数字", groups = ValidationGroup.Update.class)
    private Long id;

    @ApiModelProperty("税收优惠的集合")
    private List<Long> ids;

}
