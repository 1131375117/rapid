package cn.huacloud.taxpreference.services.producer.entity.dtos;


import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

/**
 * 政策法规数据传输对象
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesCombinationDTO {

    @ApiModelProperty("政策法规主键id")
    @NotBlank(message = "政策法规主键id不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private Long id;

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String title;

    @ApiModelProperty("文号")
    @NotBlank(message = "文号不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String docCode;

    @ApiModelProperty("所属区域名称")
    @NotBlank(message = "所属区域名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String areaName;

    @ApiModelProperty("所属区域码值")
    @NotBlank(message = "所属区域码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String areaCode;

    @ApiModelProperty("来源")
    @NotBlank(message = "来源不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String docSource;

    @ApiModelProperty("所属税种名称")
    @NotBlank(message = "所属税种名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxCategoriesName;

    @ApiModelProperty("所属税种码值")
    @NotBlank(message = "所属税种码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxCategoriesCode;

    @ApiModelProperty("纳税人资格认定类型名称")
    @NotBlank(message = "纳税人资格认定类型名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> taxpayerIdentifyTypeNames;

    @ApiModelProperty("纳税人资格认定类型码值")
    @NotBlank(message = "纳税人资格认定类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> taxpayerIdentifyTypeCodes;

    @ApiModelProperty("适用企业类型名称")
    @NotBlank(message = "适用企业类型名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> enterpriseTypeNames;

    @ApiModelProperty("适用企业类型码值")
    @NotBlank(message = "适用企业类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> enterpriseTypeCodes;

    @ApiModelProperty("适用行业名称")
    @NotBlank(message = "适用行业名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> industryNames;

    @ApiModelProperty("适用行业码值")
    @NotBlank(message = "适用行业码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> industryCodes;

    @ApiModelProperty("有效性")
    @NotBlank(message = "有效性不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String validity;

    @ApiModelProperty("发布日期")
    @NotBlank(message = "发布日期不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private LocalDate releaseDate;

    @ApiModelProperty("摘要")
    @NotBlank(message = "摘要不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String digest;

    @ApiModelProperty("正文")
    @NotBlank(message = "正文不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String content;

    @ApiModelProperty("政策法规id")
    @NotBlank(message = "政策法规id不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private Long policiesId;

    @ApiModelProperty("政策法规状态")
    @NotBlank(message = "政策法规状态不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String policiesStatus;


}
