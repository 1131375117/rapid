package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策犯规条件类
 *
 * @author wuxin
 */
@Data
@ApiModel
public class QueryPoliciesDTO extends KeywordPageQueryDTO {

    @ApiModelProperty("查询条件类型")
    private KeyWordField keyWordField;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("文号")
    private String docCode;

    @ApiModelProperty("所属税种码值")
    private String taxCategoriesCode;

    @ApiModelProperty("纳税人资格认定类型码值")
    private String taxpayerIdentifyTypeCodes;

    @ApiModelProperty("适用企业类型码值")
    private String enterpriseTypeCodes;

    @ApiModelProperty("适用行业码值")
    private String industryCodes;

    @ApiModelProperty("所属区域码值")
    private String areaCode;

    @ApiModelProperty("有效性")
    private String validity;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("更新日期")
    private LocalDateTime updateTime;

    @ApiModelProperty("排序字段")
    private SortField sortField;

    public enum SortField {
        RELEASE_DATE,
        UPDATE_TIME;
    }

    public enum KeyWordField {
        TITLE,
        DOC_CODE;
    }
}
