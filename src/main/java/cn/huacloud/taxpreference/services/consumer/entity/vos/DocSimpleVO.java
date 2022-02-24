package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangkh
 */
@Data
public class DocSimpleVO {
    @ApiModelProperty(value = "主键ID",example = "2222")
    private Long id;

    @ApiModelProperty(value = "标题",example = "标题")
    private String title;
}
