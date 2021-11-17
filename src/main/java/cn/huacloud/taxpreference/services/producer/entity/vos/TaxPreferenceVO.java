package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 税收优惠VO对象
 * @author: fuhua
 * @create: 2021-10-21 09:32
 **/
@Data
public class TaxPreferenceVO {

    /**
     * 收入税种种类名称
     */
    @ApiModelProperty("收入税种种类名称")
    private String taxCategoriesName;

    /**
     * 收入税种种类码值
     */
    @ApiModelProperty("收入税种种类码值")
    private String taxCategoriesCode;

    /**
     * 纳税人登记注册类型名称
     */
    @ApiModelProperty("纳税人登记注册类型名称")
    private String taxpayerRegisterTypeName;

    /**
     * 纳税人登记注册类型码值
     */
    @ApiModelProperty("纳税人登记注册类型码值")
    private String taxpayerRegisterTypeCode;

    /**
     * 纳税人类型名称
     */
    @ApiModelProperty("纳税人类型名称")
    private String taxpayerTypeName;

    /**
     * 纳税人类型码值
     */
    @ApiModelProperty("纳税人类型码值")
    private String taxpayerTypeCode;

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
     * 纳税信用等级
     */
    @ApiModelProperty("纳税信用等级")
    private String taxpayerCreditRatings;

    /**
     * 优惠事项名称
     */
    @ApiModelProperty("优惠事项名称")
    private String taxPreferenceName;


    /**
     * 留存备查资料
     */
    @ApiModelProperty("留存备查资料")
    private String keepQueryData;

    /**
     * 提交税务机关资料
     */
    @ApiModelProperty("提交税务机关资料")
    private String submitTaxData;

    /**
     * 资料报送时限
     */
    @ApiModelProperty("资料报送时限")
    private String submitTimeLimit;

    /**
     * 标签管理
     */
    @ApiModelProperty("标签管理")
    private String labels;

    /**
     * 减免事项
     */
    @ApiModelProperty("减免事项,多个按照逗号隔开")
    private String taxPreferenceItem;

    /**
     * 政策法规信息
     */
    @ApiModelProperty("政策法规信息")
    private List<TaxPreferencePoliciesVO> taxPreferencePoliciesVOList;

    /**
     * 申报信息
     */
    @ApiModelProperty("申报信息")
    private List<SubmitConditionVO> submitConditionVOList;

}
