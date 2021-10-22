package cn.huacloud.taxpreference.services.producer.entity.vos;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

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
    private String enterpriseTypeName;

    /**
     * 有效性
     */
    @ApiModelProperty("有效性")
    private String effectiveness;

    @ApiModelProperty("审核状态")
    private String  processStatus;

    @ApiModelProperty("发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate approvalTime;

}
