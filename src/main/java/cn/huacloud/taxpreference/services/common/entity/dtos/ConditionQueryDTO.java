package cn.huacloud.taxpreference.services.common.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 税收优惠条件查询对象
 * @author wangkh
 */
@Data
public class ConditionQueryDTO {
    @ApiModelProperty("收入种类代码")
    private List<String> taxCategoriesCodes;
    @ApiModelProperty("减免事项代码")
    private List<String> exemptMatterCodes;
}
