package cn.huacloud.taxpreference.services.common.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fuhua
 **/
@Data
public class CollectionQueryDTO extends PageQueryDTO {

    @ApiModelProperty(value = "收藏类型",notes =
    "POLICIES-政策法规,POLICIES_EXPLAIN-政策解读,FREQUENTLY_ASKED_QUESTION-热门问答,TAX_PREFERENCE-税收优惠,CASE_ANALYSIS-案例解析,CONSULTATION-专家咨询")
    private CollectionType collectionType;
}
