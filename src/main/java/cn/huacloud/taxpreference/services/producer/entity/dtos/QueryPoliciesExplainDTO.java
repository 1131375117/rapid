package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策解读VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class QueryPoliciesExplainDTO extends KeywordPageQueryDTO {

    @ApiModelProperty("政策解读id")
    private Long id;
    @ApiModelProperty("政策法规id")
    private Long policiesId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;

    @ApiModelProperty("开始日期")
    private LocalDate startTime;

    @ApiModelProperty("结束日期")
    private LocalDate endTime;


    @ApiModelProperty("更新日期")
    private LocalDateTime updateTime;

    @ApiModelProperty("排序")
    private SortField sortField;

    public enum SortField {
        RELEASE_DATE,
        UPDATE_TIME;
    }

}
