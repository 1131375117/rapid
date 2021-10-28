package cn.huacloud.taxpreference.services.producer.entity.vos;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 政策法规VO
 *
 * @author wuxin
 */
@Data
public class PoliciesDetailVO {

    /**
     * 主键ID
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty("id")
    private String title;

    /**
     * 文号
     */
    @ApiModelProperty("id")
    private String docCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("id")
    private String areaName;

    /**
     * 所属区域码值
     */
    @ApiModelProperty("id")
    private String areaCode;

    /**
     * 来源
     */
    @ApiModelProperty("id")
    private String docSource;

    /**
     * 所属税种名称
     */
    @ApiModelProperty("id")
    private String taxCategoriesName;

    /**
     * 所属税种码值
     */
    @ApiModelProperty("id")
    private String taxCategoriesCode;

    /**
     * 纳税人资格认定类型名称
     */
    @ApiModelProperty("id")
    private String taxpayerIdentifyTypeName;

    /**
     * 纳税人资格认定类型码值
     */
    @ApiModelProperty("id")
    private String taxpayerIdentifyTypeCode;

    /**
     * 适用企业类型名称
     */
    @ApiModelProperty("id")
    private String taxpayerCreditRatings;

    /**
     * 适用企业类型码值
     */
    @ApiModelProperty("id")
    private String enterpriseTypeCodes;

    /**
     * 适用行业名称
     */
    @ApiModelProperty("id")
    private String industryNames;

    /**
     * 适用行业码值
     */
    @ApiModelProperty("id")
    private String industryCodes;

    /**
     * 有效性
     */
    @ApiModelProperty("id")
    private String validity;

    /**
     * 发布日期
     */
    @ApiModelProperty("id")
    private LocalDate releaseDate;

    /**
     * 摘要
     */
    @ApiModelProperty("id")
    private String digest;

    /**
     * 正文
     */
    @ApiModelProperty("id")
    private String content;

    /**
     * 标签集合
     */
//    @ApiModelProperty("id")
//    private String labels;

}
