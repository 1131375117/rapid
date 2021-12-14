package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收藏DTO
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
public class CollectionVO {

    /**
     * 收藏类型
     */
    @ApiModelProperty(notes = "收藏类型,POLICIES-政策法规,POLICIES_EXPLAIN-政策解读,FREQUENTLY_ASKED_QUESTION-热门问答" +
            "TAX_PREFERENCE-税收优惠,CASE_ANALYSIS-案例分析")
    private CollectionType collectionType;

    /**
     * 数据源id
     */
    @ApiModelProperty("数据源id")

    private Long sourceId;

}
