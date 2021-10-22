package cn.huacloud.taxpreference.services.producer.entity.dtos;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesDTO {

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

}
