package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 税收优惠VO对象
 * @author: fuhua
 * @create: 2021-10-21 09:32
 **/
@Data
public class TaxPreferenceVO {
    @ApiModelProperty("税收优惠id")
    @NotNull(message = "税收优惠id不能为空",groups = ValidationGroup.Manual.class)
    private Long id;

    /**
     * 收入税种种类名称
     */
    @ApiModelProperty("收入税种种类名称")
    @NotEmpty(message = "收入税种种类名称不能为空",groups = ValidationGroup.Manual.class)
    private List<@Valid String> taxCategoriesNames;

    /**
     * 收入税种种类码值
     */
    @ApiModelProperty("收入税种种类码值")
    @NotEmpty(message = "收入税种种类码值不能为空",groups = ValidationGroup.Manual.class)
    private List<@Valid String> taxCategoriesCodes;

    /**
     * 纳税人登记注册类型名称
     */
    @ApiModelProperty("纳税人登记注册类型名称")
    @NotEmpty(message = "纳税人登记注册类型名称不能为空",groups = ValidationGroup.Manual.class)
    private String taxpayerRegisterTypeName;

    /**
     * 纳税人登记注册类型码值
     */
    @ApiModelProperty("纳税人登记注册类型码值")
    @NotEmpty(message = "纳税人登记注册类型码值不能为空",groups = ValidationGroup.Manual.class)
    private String taxpayerRegisterTypeCode;

    /**
     * 纳税人类型名称
     */
    @ApiModelProperty("纳税人类型名称")
    @NotEmpty(message = "纳税人类型名称不能为空",groups = ValidationGroup.Manual.class)
    private String taxpayerTypeName;

    /**
     * 纳税人类型码值
     */
    @ApiModelProperty("纳税人类型码值")
    @NotEmpty(message = "纳税人类型码值不能为空",groups = ValidationGroup.Manual.class)
    private String taxpayerTypeCode;

    /**
     * 适用行业名称
     */
    @ApiModelProperty("适用行业名称")
    @NotEmpty(message = "适用行业名称不能为空",groups = ValidationGroup.Manual.class)
    private List<@Valid String> industryNames;

    /**
     * 适用行业码值
     */
    @ApiModelProperty("适用行业码值")
    @NotEmpty(message = "适用行业码值不能为空",groups = ValidationGroup.Manual.class)
    private List<@Valid String> industryCodes;

    /**
     * 适用企业类型名称
     */
    @ApiModelProperty("适用企业类型名称")
    @NotEmpty(message = "适用企业类型名称不能为空",groups = ValidationGroup.Manual.class)
    private List<@Valid String> enterpriseTypeNames;

    /**
     * 适用企业类型码值
     */
    @ApiModelProperty("适用企业类型码值")
    @NotEmpty(message = "适用企业类型码值不能为空",groups = ValidationGroup.Manual.class)
    private List<@Valid String> enterpriseTypeCodes;

    /**
     * 纳税信用等级
     */
    @ApiModelProperty("纳税信用等级")
    @NotEmpty(message = "纳税信用等级不能为空",groups = ValidationGroup.Manual.class)
    private List<@Valid String> taxpayerCreditRatings;

    /**
     * 优惠事项名称
     */
    @ApiModelProperty("优惠事项名称")
    @NotEmpty(message = "优惠事项名称不能为空",groups = ValidationGroup.Manual.class)
    private String taxPreferenceName;


    /**
     * 留存备查资料
     */
    @ApiModelProperty("留存备查资料")
    @NotEmpty(message = "留存备查资料不能为空",groups = ValidationGroup.Manual.class)
    private String keepQueryData;

    /**
     * 提交税务机关资料
     */
    @ApiModelProperty("提交税务机关资料")
    @NotEmpty(message = "提交税务机关资料不能为空",groups = ValidationGroup.Manual.class)
    private String submitTaxData;

    /**
     * 资料报送时限
     */
    @ApiModelProperty("资料报送时限")
    @NotEmpty(message = "资料报送时限不能为空",groups = ValidationGroup.Manual.class)
    private String submitTimeLimit;

    /**
     * 标签管理
     */
    @ApiModelProperty("标签管理")
    private List<String> labels;

    /**
     * 减免事项
     */
    @ApiModelProperty("减免事项")
    @NotEmpty(message = "减免事项不能为空",groups = ValidationGroup.Manual.class)
    private String taxPreferenceItem;

    @ApiModelProperty("申报简要说明")
    @NotEmpty(message = "申报简要说明不能为空",groups = ValidationGroup.Manual.class)
    private String submitDescription;

    @ApiModelProperty("有效性")
    @NotNull(message = "有效性不能为空",groups = ValidationGroup.Manual.class)
    private PreferenceValidation validity;

    /**
     * 政策法规信息
     */
    @ApiModelProperty("政策法规信息")
    @NotEmpty(message = "政策法规信息不能为空",groups = ValidationGroup.Manual.class)
    private List<@Valid TaxPreferencePoliciesVO> taxPreferencePoliciesVOList;

    /**
     * 申报信息
     */
    @ApiModelProperty("申报信息")
    @NotEmpty(message = "申报信息",groups = ValidationGroup.Manual.class)
    private List<@Valid SubmitConditionVO> submitConditionVOList;

}
