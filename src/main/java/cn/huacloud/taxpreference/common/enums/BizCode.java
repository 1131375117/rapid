package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.common.utils.ResultVO;

/**
 * 业务状态码值
 * @author wangkh
 */
public enum BizCode {

    // 2xx 提示信息码值
    _200(200, "OK"),

    // 41xx 通用业务异常
    _4100(4100, "请求参数错误"),

    // 42xx 用户服务端异常
    _4200(4200, "用户名或密码不正确"),

    // 42xx 生产者服务
    _4300(4300, ""),


    // 5xx 系统错误
    _500(500, "服务端异常");

    public final int code;
    public final String msg;

    BizCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public <T> ResultVO<T> getResultVO(T data) {
        return ResultVO.ok(this.code, this.msg, data);
    }

    public ResultVO<Void> getResultVO() {
        return ResultVO.ok(this.code, this.msg, null);
    }

    public TaxPreferenceException exception() {
        TaxPreferenceException bizException = new TaxPreferenceException(msg);
        bizException.setCode(code);
        return bizException;
    }
}
