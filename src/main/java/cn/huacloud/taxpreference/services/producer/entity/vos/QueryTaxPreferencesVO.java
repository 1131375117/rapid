package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import cn.huacloud.taxpreference.common.enums.taxpreference.ReleaseMatter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 税收优惠列表VO
 * @author: fuhua
 * @create: 2021-10-22 14:29
 **/
@Data
public class QueryTaxPreferencesVO {

    /**
     * 优惠事项名称
     */
    @ApiModelProperty("优惠事项名称")
    private String taxPreferenceName;

    @ApiModelProperty("优惠事项id")
    private Long id;

    /**
     * 纳税人登记注册类型名称
     */
    @ApiModelProperty("纳税人登记注册类型名称")
    private String taxpayerRegisterTypeName;

    /**
     * 收入税种种类名称
     */
    @ApiModelProperty("收入税种种类名称")
    private String taxCategoriesName;

    /**
     * 适用企业类型名称
     */
    @ApiModelProperty("适用企业类型名称")
    private String enterpriseTypeNames;

    /**
     * 适用企业类型名称
     */
    @ApiModelProperty("适用行业名称")
    private String industryNames;

    /**
     * 纳税信用等级
     */
    @ApiModelProperty("纳税信用等级")
    private String taxpayerCreditRatings;

    /**
     * 有效性
     */
    @ApiModelProperty("有效性")
    private PreferenceValidation validity;

    @ApiModelProperty("审核状态")
    private String processStatus;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("发布状态:RELEASED-已发布;UNRELEASED-未发布")
    private ReleaseMatter taxPreferenceStatus;

}
