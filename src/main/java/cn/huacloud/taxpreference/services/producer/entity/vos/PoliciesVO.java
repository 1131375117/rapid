package cn.huacloud.taxpreference.services.producer.entity.vos;


import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 政策法规VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesVO extends PoliciesCommonListVO{

    @ApiModelProperty("政策法规Id")
    private Long id;
    @ApiModelProperty("文号")
    private String docCode;
    @ApiModelProperty("字号")
    private String docWordCode;
    @ApiModelProperty("年号")
    private Integer docYearCode;
    @ApiModelProperty("编号")
    private Integer docNumCode;
    @ApiModelProperty("所属税种名称")
    private String taxCategoriesNames;
    @ApiModelProperty("所属区域名称")
    private String areaName;
    @ApiModelProperty("有效性")
    private ValidityEnum validity;

}
