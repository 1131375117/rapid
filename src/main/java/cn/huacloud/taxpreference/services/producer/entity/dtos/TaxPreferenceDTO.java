package cn.huacloud.taxpreference.services.producer.entity.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
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
    private Long id;

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
     * 申报表填写简要说明-政策法规
     */
    @ApiModelProperty("申报表填写简要说明")
    private String submitDescription;

    /**
     * 政策法规ID-政策法规
     */
    @ApiModelProperty("政策法规ID")
    private Long policiesId;

    /**
     * 有效期起-政策法规
     */
    @ApiModelProperty("有效期起")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="Asia/Shanghai")
    private Date validityBeginDate;

    /**
     * 有效期至-政策法规
     */
    @ApiModelProperty("有效期至")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="Asia/Shanghai")
    private Date validityEndDate;

    /**
     * 申报信息
     */
    @ApiModelProperty("申报信息")
    private List<SubmitConditionDTO> submitConditionDTOList;


}
