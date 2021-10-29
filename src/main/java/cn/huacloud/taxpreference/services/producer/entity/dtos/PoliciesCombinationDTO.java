package cn.huacloud.taxpreference.services.producer.entity.dtos;


import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.List;

/**
 * 政策法规数据传输对象组合
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesCombinationDTO {

    @ApiModelProperty("政策法规主键id")
//    @NotEmpty(message = "政策法规主键id不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private Long id;

    @ApiModelProperty("标题")

    private String title;

    @ApiModelProperty("文号")
    @NotEmpty(message = "文号不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String docCode;


    @ApiModelProperty("所属区域码值")
    @NotEmpty(message = "所属区域码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String areaCode;
    private String areaName = "a";

    @ApiModelProperty("来源")
    @NotEmpty(message = "来源不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String docSource;


    @ApiModelProperty("所属税种码值")
    @NotEmpty(message = "所属税种码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxCategoriesCode;
    private String taxCategoriesName = "a";


    @ApiModelProperty("纳税人资格认定类型码值")
    @NotEmpty(message = "纳税人资格认定类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> taxpayerIdentifyTypeCodes;
    private String taxpayerIdentifyTypeNames = "a";


    @ApiModelProperty("适用企业类型码值")
    @NotEmpty(message = "适用企业类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> enterpriseTypeCodes;
    private String enterpriseTypeNames = "a";


    @ApiModelProperty("适用行业码值")
    @NotEmpty(message = "适用行业码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> industryCodes;
    private String industryNames = "a";

    @ApiModelProperty("有效性")
    @NotEmpty(message = "有效性不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String validity;

    @ApiModelProperty("发布日期")
    @NotNull(message = "发布日期不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private LocalDate releaseDate;

    @ApiModelProperty("摘要")
    @NotEmpty(message = "摘要不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String digest;

    @ApiModelProperty("正文")
    @NotEmpty(message = "正文不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String content;

    @ApiModelProperty("政策法规id")
//    @NotEmpty(message = "政策法规id不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private Long policiesId;

    @ApiModelProperty("政策法规状态")
    @NotEmpty(message = "政策法规状态不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String policiesStatus;
    private List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOList;


}
