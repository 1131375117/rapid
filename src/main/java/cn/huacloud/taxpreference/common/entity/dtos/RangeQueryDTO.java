package cn.huacloud.taxpreference.common.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 范围查询对象
 * @author wangkh
 */
@Data
public class RangeQueryDTO<T> {
    @ApiModelProperty("开始")
    private T from;
    @ApiModelProperty("结束")
    private T to;
}
