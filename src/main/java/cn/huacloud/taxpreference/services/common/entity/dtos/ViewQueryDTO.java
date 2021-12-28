package cn.huacloud.taxpreference.services.common.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.enums.ViewType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fuhua
 **/
@Data
public class ViewQueryDTO extends PageQueryDTO {

    @ApiModelProperty(notes = "浏览类型,POLICIES-政策法规,POLICIES_EXPLAIN-政策解读,FREQUENTLY_ASKED_QUESTION-热门问答" +
            "TAX_PREFERENCE-税收优惠,CASE_ANALYSIS-案例分析")
    private ViewType viewType;
}
