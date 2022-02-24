package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

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

    @ApiModelProperty("数据标题")
    private String title;

    @ApiModelProperty("收藏时间")
    private LocalDate collectionTime;

    @ApiModelProperty("文档来源")
    private String docSource;

}
