package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

/**
 * @description: 税收优惠列表DTO
 * @author: fuhua
 * @create: 2021-10-22 14:29
 **/
@Data
public class QueryTaxPrefrencesDTO extends KeywordPageQueryDTO {
    /**
     * 收入税种种类码值
     */
    @ApiModelProperty("收入税种种类码值")
    private String taxCategoriesCode;

    /**
     * 纳税人登记注册类型码值
     */
    @ApiModelProperty("纳税人登记注册类型码值")
    private String taxpayerRegisterTypeCode;

    /**
     * 纳税人类型码值
     */
    @ApiModelProperty("纳税人类型码值")
    private String taxpayerTypeCode;

    /**
     * 适用行业码值
     */
    @ApiModelProperty("适用行业码值")
    private String industryCode;

    /**
     * 适用企业类型码值
     */
    @ApiModelProperty("适用企业类型码值")
    private String enterpriseTypeCode;

    /**
     * 纳税信用等级
     */
    @ApiModelProperty("纳税信用等级")
    @NotEmpty(message = "适纳税信用等级不能为空",groups = {ValidationGroup.Update.class,ValidationGroup.Create.class})
    private String taxpayerCreditRating;

    /**
     * 有效性
     */
    @ApiModelProperty("有效性")
    private String validity;

    /**
     * 创建人用户id
     */
    @ApiModelProperty("有效性")
    private String inputUserId;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 查询类型
     */
    @ApiModelProperty("查询类型")
    private Integer queryType;

}
