package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangkh
 */
@Getter
@Setter
public class LatestTaxPreferenceSearchQueryDTO extends PageQueryDTO {
    @ApiModelProperty("所属税种")
    private String taxCategoriesCode;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (StringUtils.isBlank(taxCategoriesCode)) {
            taxCategoriesCode = null;
        }
    }
}
