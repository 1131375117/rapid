package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDate;

/**
 * 政策解读VO
 * @author wuxin
 */
@Data
@ApiModel
public class QueryPoliciesExplainDTO extends KeywordPageQueryDTO {

    @ApiModelProperty("查询标题")
    private String title;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("查询发布日期")
    private LocalDate releaseDate;

    @ApiModelProperty("排序")
    private SortField sortField;

    public enum SortField {
        RELEASE_DATE,
        UPDATE_TIME;
    }

}
