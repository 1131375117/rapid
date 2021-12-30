package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文号视图对象
 * @author wangkh
 */
@Data
public class DocCodeVO {
    @ApiModelProperty("字号")
    private String docWordCode;
    @ApiModelProperty("年号")
    private Integer docYearCode;
    @ApiModelProperty("序号")
    private Integer docNumCode;
}
