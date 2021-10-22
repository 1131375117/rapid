package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.SubmitConditionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferencePoliciesDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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
    private String industryName;

    /**
     * 适用行业码值
     */
    @ApiModelProperty("适用行业码值")
    private String industryCode;

    /**
     * 适用企业类型名称
     */
    @ApiModelProperty("适用企业类型名称")
    private String enterpriseTypeName;

    /**
     * 适用企业类型码值
     */
    @ApiModelProperty("适用企业类型码值")
    private String enterpriseTypeCode;

    /**
     * 纳税信用等级
     */
    @ApiModelProperty("纳税信用等级")
    private String taxpayerCreditRating;

    /**
     * 优惠事项名称
     */
    @ApiModelProperty("优惠事项名称")
    private String taxPreferenceName;

    /**
     * 具体优惠内容摘要
     */
    @ApiModelProperty("具体优惠内容摘要")
    private String digest;

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
     * 政策法规信息
     */
    @ApiModelProperty("申报信息")
    private List<TaxPreferencePoliciesVO> taxPreferencePoliciesVOList;

    /**
     * 申报信息
     */
    @ApiModelProperty("申报信息")
    private List<SubmitConditionVO> submitConditionVOList;


}
