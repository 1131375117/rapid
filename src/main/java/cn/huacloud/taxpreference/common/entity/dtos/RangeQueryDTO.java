package cn.huacloud.taxpreference.common.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 范围查询对象
 * @author wangkh
 */
@Data
public class RangeQueryDTO<T> {
    @ApiModelProperty(value = "开始",example = "1920-03-01")
    private T from;
    @ApiModelProperty(value = "结束",example = "2025-03-01")
    private T to;
}
