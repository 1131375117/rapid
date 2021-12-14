package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

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
    private Long policiesId;

    /**
     * 政策法规ID-政策法规
     */
    @ApiModelProperty("政策法规name")
    private String policiesName;

    @ApiModelProperty("政策法规文号")
    private String docCode;
    /**
     * 有效期起-政策法规
     */
    @ApiModelProperty("有效期起")
    private LocalDate validityBeginDate;

    /**
     * 有效期至-政策法规
     */
    @ApiModelProperty("有效期至")
    private LocalDate validityEndDate;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Long sort;
    /**
     * 具体优惠内容摘要
     */
    @ApiModelProperty("具体优惠内容摘要")
    private String digest;

    /**
     * 政策法规关联具体交款
     */
    @ApiModelProperty("政策法规关联具体交款")
    private String policiesItems;

}
