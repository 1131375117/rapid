package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import cn.huacloud.taxpreference.common.enums.taxpreference.ReleaseMatter;
import cn.huacloud.taxpreference.common.enums.taxpreference.SortType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 税收优惠列表DTO
 * @author: fuhua
 * @create: 2021-10-22 14:29
 **/
@Data
public class QueryTaxPreferencesDTO extends KeywordPageQueryDTO {
    /**
     * 收入税种种类码值
     */
    @ApiModelProperty("收入税种种类码值")
    private List<String> taxCategoriesCodes;

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
    private List<String> industryCodes;

    /**
     * 适用企业类型码值
     */
    @ApiModelProperty("适用企业类型码值")
    private List<String> enterpriseTypeCodes;

    /**
     * 纳税信用等级
     */
    @ApiModelProperty("纳税信用等级A, B, C, D, M")
    private List<String> taxpayerCreditRatings;

    /**
     * 有效性
     */
    @ApiModelProperty("有效性-(EFFECTIVE-有效,INVALID-无效)")
    private PreferenceValidation validity;

    /**
     * 排序
     */
    @ApiModelProperty("排序字段->默认->CREATE_TIME,UPDATE_TIME")
    private SortType sortType;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    /**
     * 截止时间
     */
    @ApiModelProperty("截止时间")
    private LocalDateTime endTime;

    @ApiModelProperty("是否自我发布-RELEASED:我的已发布,UNRELEASED-我的未发布,null-全部<-默认输入")
    private ReleaseMatter releaseMatter;


}
