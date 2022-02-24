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
    @ApiModelProperty(value = "标签名称", example = "制造业中型企业")
    private String labelName;

    @ApiModelProperty(value = "热度评分", example = "1")
    private Long hotScore;
}
