package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description: 税收优惠政策法规关联表VO
 * @author: fuhua
 * @create: 2021-10-21 09:59
 **/
@Data
public class TaxPreferencePoliciesVO {

    /**
     * 政策法规ID-政策法规
     */
    @ApiModelProperty("政策法规ID")
    @NotNull(message = "政策法规ID不能为空",groups = ValidationGroup.Manual.class)
    private Long policiesId;

    /**
     * 政策法规ID-政策法规
     */
    @ApiModelProperty("政策法规name")
    @NotEmpty(message = "政策法规name不能为空",groups = ValidationGroup.Manual.class)
    private String policiesName;

    @ApiModelProperty("政策法规文号")
    private String docCode;
    /**
     * 有效期起-政策法规
     */
    @ApiModelProperty("有效期起")
    @NotNull(message = "有效期起不能为空",groups = ValidationGroup.Manual.class)
    private String validityBeginDate;

    /**
     * 有效期至-政策法规
     */
    @ApiModelProperty("有效期至")
    private String validityEndDate;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    @NotNull(message = "排序字段不能为空",groups = ValidationGroup.Manual.class)
    private Long sort;
    /**
     * 具体优惠内容摘要
     */
    @ApiModelProperty("具体优惠内容摘要")
  //  @NotEmpty(message = "具体优惠内容摘要不能为空",groups = ValidationGroup.Manual.class)
    private String digest;

    /**
     * 政策法规关联具体交款
     */
    @ApiModelProperty("政策法规关联具体条款")
    private String policiesItems;

    /**
     * 政策法规日期类型
     */
    @ApiModelProperty("日期类型")
    private Boolean dateType;

}
