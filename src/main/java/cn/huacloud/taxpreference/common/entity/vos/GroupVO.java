package cn.huacloud.taxpreference.common.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class GroupVO<T> {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("数值")
    private List<T> values;
}
