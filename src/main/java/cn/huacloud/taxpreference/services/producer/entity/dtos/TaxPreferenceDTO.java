package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import cn.huacloud.taxpreference.common.enums.taxpreference.TaxStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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

    @ApiModelProperty("创建人用户,新增不传")
    private Long inputUserId;

    @ApiModelProperty("有效性-EFFECTIVE有效,INVALID无效")
    private PreferenceValidation validity;

    /**
     * 收入税种种类码值
     */
    @ApiModelProperty("收入税种种类码值")
    @NotEmpty(message = "收入税种种类码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> taxCategoriesCodes;


    /**
     * 纳税人登记注册类型码值
     */
    @ApiModelProperty("纳税人登记注册类型码值")
    //@NotEmpty(message = "纳税人登记注册类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxpayerRegisterTypeCode;


    /**
     * 纳税人类型码值
     */
    @ApiModelProperty("纳税人类型码值")
   // @NotEmpty(message = "纳税人类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxpayerTypeCode;


    /**
     * 适用行业码值
     */
    @ApiModelProperty("适用行业码值")
  //  @NotEmpty(message = "适用行业码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<String> industryCodes;


    /**
     * 适用企业类型码值
     */
    @ApiModelProperty("适用企业类型")
    @NotEmpty(message = "适用企业类型码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String enterpriseType;

    /**
     * 优惠事项名称
     */
    @ApiModelProperty("优惠事项名称")
    @NotEmpty(message = "优惠事项名称不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxPreferenceName;


    /**
     * 留存备查资料
     */
    @ApiModelProperty("留存备查资料")
  //  @NotEmpty(message = "留存备查资料不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String keepQueryData;

    /**
     * 提交税务机关资料
     */
    @ApiModelProperty("提交税务机关资料")
   // @NotEmpty(message = "提交税务机关资料不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String submitTaxData;

    /**
     * 标签管理
     */
    @ApiModelProperty("标签管理")
    private List<String> labels;

    /**
     * 减免事项
     */
    @ApiModelProperty("减免事项")
    @NotEmpty(message = "减免事项", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String taxPreferenceItem;


    /**
     * 资料报送时限
     */
    @ApiModelProperty("资料报送时限")
   // @NotEmpty(message = "提资料报送时限不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String submitTimeLimit;

    @ApiModelProperty("申报表填写指导说明")
    @NotEmpty(message = "申报表填写指导说明不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private String submitDescription;

    @ApiModelProperty("政策法规关联信息")
    @NotEmpty(message = "政策法规关联信息不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<@Valid TaxPreferencePoliciesDTO> taxPreferencePoliciesDTOList;
    /**
     * 申报信息
     */
    @ApiModelProperty("申报信息")
  //  @NotEmpty(message = "申报信息不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    private List<SubmitConditionDTO> submitConditionDTOList;

    @ApiModelProperty("SAVE-保存或者SUBMIT提交")
    private  TaxStatus status;

    @ApiModelProperty("固定条件信息")
    private List<ConditionDO> conditionList;


}
