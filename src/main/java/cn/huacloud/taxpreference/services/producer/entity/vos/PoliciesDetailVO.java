package cn.huacloud.taxpreference.services.producer.entity.vos;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 政策法规VO
 *
 * @author wuxin
 */
@Data
public class PoliciesDetailVO {

    /**
     * 主键Id
     */
    @ApiModelProperty("政策法规主键id")
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 文号
     */
    @ApiModelProperty("文号")
    private String docCode;

    /**
     * 所属区域名称
     */
    @ApiModelProperty("所属区域名称")
    private String areaName;

    /**
     * 所属区域码值
     */
    @ApiModelProperty("所属区域码值")
    private String areaCode;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String docSource;

    /**
     * 所属税种名称
     */
    @ApiModelProperty("所属税种名称")
    private String taxCategoriesName;

    /**
     * 所属税种码值
     */
    @ApiModelProperty("所属税种码值")
    private String taxCategoriesCode;

    /**
     * 纳税人资格认定类型名称
     */
    @ApiModelProperty("纳税人资格认定类型名称")
    private String taxpayerIdentifyTypeNames;

    /**
     * 纳税人资格认定类型码值
     */
    @ApiModelProperty("纳税人资格认定类型码值")
    private String taxpayerIdentifyTypeCodes;

    /**
     * 适用企业类型名称
     */
    @ApiModelProperty("适用企业类型名称")
    private String enterpriseTypeNames;

    /**
     * 适用企业类型码值
     */
    @ApiModelProperty("适用企业类型码值")
    private String enterpriseTypeCodes;

    /**
     * 适用行业名称
     */
    @ApiModelProperty("适用行业名称")
    private String industryNames;

    /**
     * 适用行业码值
     */
    @ApiModelProperty("适用行业码值")
    private String industryCodes;

    /**
     * 有效性
     */
    @ApiModelProperty("有效性")
    private String validity;

    /**
     * 发布日期
     */
    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;

    /**
     * 摘要
     */
    @ApiModelProperty("摘要")
    private String digest;

    /**
     * 正文
     */
    @ApiModelProperty("正文")
    private String content;


    /**
     * 废止信息
     */
    @ApiModelProperty("废止信息")
    private String abolishNote;

    @ApiModelProperty("标签管理")
    private String labels;

}
