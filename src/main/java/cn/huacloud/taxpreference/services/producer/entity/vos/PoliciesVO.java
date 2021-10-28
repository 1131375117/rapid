package cn.huacloud.taxpreference.services.producer.entity.vos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * 政策法规VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesVO {

    @ApiModelProperty("政策法规ID")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("文号")
    private String docCode;
    @ApiModelProperty("所属税种名称")
    private String taxCategoriesName;
    @ApiModelProperty("所属税种码值")
    private String taxCategoriesCode;
    @ApiModelProperty("所属区域名称")
    private String areaName;
    @ApiModelProperty("所属区域码值")
    private String areaCode;
    @ApiModelProperty("有效性")
    private String validity;
    @ApiModelProperty("发布时间")
    private LocalDate releaseDate;

}
