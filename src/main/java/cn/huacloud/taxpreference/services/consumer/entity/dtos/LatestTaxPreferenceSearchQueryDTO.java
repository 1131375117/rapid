package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wangkh
 */
@Getter
@Setter
public class LatestTaxPreferenceSearchQueryDTO extends PageQueryDTO {
    @ApiModelProperty("所属税种")
    private List<String> taxCategoriesCodes;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (CollectionUtils.isEmpty(taxCategoriesCodes)) {
            taxCategoriesCodes = null;
        }
    }
}
