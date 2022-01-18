package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 政策法规摘要视图，该对象为税收优惠的子对象
 * @author wangkh
 */
@Data
public class PoliciesDigestSearchVO {

    /**
     * 政策ID
     */
    @ApiModelProperty("政策ID")
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;
    /**
     * 文号
     */
    @ApiModelProperty("文号")
    private String docCode;
    /**
     * 有效期起
     */
    @ApiModelProperty("有效期起")
    private String validityBeginDate;
    /**
     * 有效期至
     */
    @ApiModelProperty("有效期至")
    private String validityEndDate;
    /**
     * 税收优惠摘要
     */
    @ApiModelProperty("税收优惠摘要")
    private String digest;

    @ApiModelProperty("具体条款")
    private String policiesItems;
}
