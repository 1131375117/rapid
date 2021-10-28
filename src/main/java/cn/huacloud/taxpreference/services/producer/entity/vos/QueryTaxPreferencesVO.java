package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<String> enterpriseTypeName;

    /**
     * 有效性
     */
    @ApiModelProperty("有效性")
    private String validity;

    @ApiModelProperty("审核状态")
    private String processStatus;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
