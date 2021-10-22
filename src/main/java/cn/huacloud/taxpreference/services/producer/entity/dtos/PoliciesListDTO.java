package cn.huacloud.taxpreference.services.producer.entity.dtos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * 政策法规数据传输对象
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesListDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("文号")
    private String docCode;

    @ApiModelProperty("所属区域名称")
    private String areaName;

    @ApiModelProperty("所属区域码值")
    private String areaCode;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("所属税种名称")
    private String taxCategoriesName;

    @ApiModelProperty("所属税种码值")
    private String taxCategoriesCode;

    @ApiModelProperty("纳税人资格认定类型名称")
    private String taxpayerIdentifyTypeName;

    @ApiModelProperty("纳税人资格认定类型码值")
    private String taxpayerIdentifyTypeCode;

    @ApiModelProperty("适用企业类型名称")
    private String enterpriseTypeName;

    @ApiModelProperty("适用企业类型码值")
    private String enterpriseTypeCode;

    @ApiModelProperty("适用行业名称")
    private String industryName;

    @ApiModelProperty("适用行业码值")
    private String industryCode;

    @ApiModelProperty("有效性")
    private String validity;

    @ApiModelProperty("发布日期")
    private Date releaseDate;

    @ApiModelProperty("摘要")
    private String digest;

    @ApiModelProperty("正文")
    private String content;

    @ApiModelProperty("政策法规id")
    private Long policiesId;

    @ApiModelProperty("问题")
    private String question;

    @ApiModelProperty("回答")
    private String answer;



}
