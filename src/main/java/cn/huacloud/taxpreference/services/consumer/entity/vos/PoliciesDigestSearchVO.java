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
    @ApiModelProperty(value = "政策ID",example = "39664")
    private Long id;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题",example = "国家税务总局太原市税务局关于修改部分税收规范性文件的公告")
    private String title;
    /**
     * 文号
     */
    @ApiModelProperty(value = "文号",example = "国家税务总局太原市税务局〔2018〕5号")
    private String docCode;
    /**
     * 有效期起
     */
    @ApiModelProperty(value = "有效期起",example = "2022-01-13")
    private String validityBeginDate;
    /**
     * 有效期至
     */
    @ApiModelProperty(value = "有效期至")
    private String validityEndDate;
    /**
     * 税收优惠摘要
     */
    @ApiModelProperty(value = "税收优惠摘要",example = "税收优惠摘要")
    private String digest;

    @ApiModelProperty(value = "具体条款",example = "税收优惠具体条款")
    private String policiesItems;
}
