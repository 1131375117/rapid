package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 税收优惠dto对象
 * @author: fuhua
 * @create: 2021-10-21 09:32
 **/
@Data
public class TaxPreferenceDTO {
    /**
     * id
     */
    @ApiModelProperty("id")
    @NotNull(message = "id不能为空", groups = ValidationGroup.Update.class)
    @Min(value = 1, message = "id必须为数字", groups = ValidationGroup.Update.class)
    private Long id;

    private Long inputUserId;

    @ApiModelProperty("有效性-有效,无效")
    @NotBlank(message = "有效性不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String validity;

    /**
     * 收入税种种类名称
     */
    @ApiModelProperty("收入税种种类名称")
    @NotBlank(message = "收入税种种类名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxCategoriesName;

    /**
     * 收入税种种类码值
     */
    @ApiModelProperty("收入税种种类码值")
    @NotEmpty(message = "收入税种种类码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxCategoriesCode;

    /**
     * 纳税人登记注册类型名称
     */
    @ApiModelProperty("纳税人登记注册类型名称")
    @NotEmpty(message = "纳税人登记注册类型名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxpayerRegisterTypeName;

    /**
     * 纳税人登记注册类型码值
     */
    @ApiModelProperty("纳税人登记注册类型码值")
    @NotEmpty(message = "纳税人登记注册类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxpayerRegisterTypeCode;

    /**
     * 纳税人类型名称
     */
    @ApiModelProperty("纳税人类型名称")
    @NotEmpty(message = "纳税人类型名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxpayerTypeName;

    /**
     * 纳税人类型码值
     */
    @ApiModelProperty("纳税人类型码值")
    @NotEmpty(message = "纳税人类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxpayerTypeCode;


    /**
     * 适用行业码值
     */
    @ApiModelProperty("适用行业码值")
    @NotEmpty(message = "适用行业码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> industryCodes;


    /**
     * 适用企业类型码值
     */
    @ApiModelProperty("适用企业类型码值")
    @NotEmpty(message = "适用企业类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> enterpriseTypeCodes;

    /**
     * 纳税信用等级
     */
    @ApiModelProperty("纳税信用等级")
    @NotEmpty(message = "适纳税信用等级不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> taxpayerCreditRatings;

    /**
     * 优惠事项名称
     */
    @ApiModelProperty("优惠事项名称")
    @NotEmpty(message = "优惠事项名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxPreferenceName;

    /**
     * 具体优惠内容摘要
     */
    @ApiModelProperty("具体优惠内容摘要")
    @NotEmpty(message = "具体优惠内容摘要不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String digest;

    /**
     * 留存备查资料
     */
    @ApiModelProperty("留存备查资料")
    @NotEmpty(message = "留存备查资料不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String keepQueryData;

    /**
     * 提交税务机关资料
     */
    @ApiModelProperty("提交税务机关资料")
    @NotEmpty(message = "提交税务机关资料不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String submitTaxData;

    /**
     * 标签管理
     */
    @ApiModelProperty("标签管理")
    @NotEmpty(message = "标签管理",groups = {ValidationGroup.Update.class,ValidationGroup.Create.class})
    private String labels;


    /**
     * 资料报送时限
     */
    @ApiModelProperty("资料报送时限")
    @NotEmpty(message = "提资料报送时限不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String submitTimeLimit;

    @ApiModelProperty("申报表填写简要说明")
    @NotEmpty(message = "申报表填写简要说明不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String submitDescription;

    @ApiModelProperty("政策法规关联信息")
    @NotEmpty(message = "政策法规关联信息不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<TaxPreferencePoliciesDTO> taxPreferencePoliciesDTOList;
    /**
     * 申报信息
     */
    @ApiModelProperty("申报信息")
    @NotEmpty(message = "申报信息不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<SubmitConditionDTO> submitConditionDTOList;


}
