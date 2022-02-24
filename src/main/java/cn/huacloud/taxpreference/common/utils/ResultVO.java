package cn.huacloud.taxpreference.common.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 前端统一返回结果
 * @author wangkh
 */
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class ResultVO<T> {

    /**
     * 状态码
     */
    @ApiModelProperty(value = "状态码")
    private int code;
    /**
     * 描述信息
     */
    @ApiModelProperty(value = "描述信息")
    private String msg;
    /**
     * 返回数据
     */
    @ApiModelProperty("响应数据体")
    private T data;

    public static <T> ResultVO<T> ok(int code, String msg, T data) {
        return new ResultVO<>(code, msg, data);
    }

    public static <T> ResultVO<T> ok(T data) {
        return ok(200, "OK", data);
    }

    public static ResultVO<Void> ok() {
        return ok(null);
    }

}
