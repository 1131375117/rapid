package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangkh
 */
@Data
public class HotLabelVO {
    @ApiModelProperty("标签名称")
    private String labelName;

    @ApiModelProperty("热度评分")
    private Integer hotScore;
}
