package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策犯规查询条件类
 * @author wuxin
 */
@Data
@ApiModel
public class QueryPoliciesDTO extends KeywordPageQueryDTO {

    @ApiModelProperty("查询条件类型")
    private KeyWordField keyWordField;

    @ApiModelProperty("查询标题")
    private String title;

    @ApiModelProperty("查询文号")
    private String docCode;

    @ApiModelProperty("查询所属税种码值")
    private String taxCategoriesCode;

    @ApiModelProperty("查询纳税人资格认定类型码")
    private String taxpayerIdentifyTypeCode;

    @ApiModelProperty("查询适用企业类型码值")
    private String enterpriseTypeCode;

    @ApiModelProperty("查询适用行业码值")
    private String industryCode;

    @ApiModelProperty("查询所属区域码值")
    private String areaCode;

    @ApiModelProperty("查询有效性")
    private String validity;

    @ApiModelProperty("查询发布日期")
    private LocalDate releaseDate;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("查询更新日期")
    private LocalDateTime updateTime;

    private SortField sortField;

    public enum SortField {
        RELEASE_DATE,
        UPDATE_TIME;
    }
    public enum KeyWordField{
        TITLE,
        DOC_CODE;
    }
}
