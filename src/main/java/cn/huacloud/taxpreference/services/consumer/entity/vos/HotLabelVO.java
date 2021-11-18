package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class HotLabelVO {
    @ApiModelProperty("标签名称")
    private String labelName;

    @ApiModelProperty("热度评分")
    private Long hotScore;
}
