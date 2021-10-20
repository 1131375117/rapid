package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

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
    _4200(4200, "用户未登录"),
    _4201(4201, "无此角色：{}"),
    _4202(4202, "无此权限：{}"),
    _4203(4203, "账号被封禁：{}秒后解封"),
    _4204(4204, "用户名或密码不正确"),

    // 43xx 生产者服务
    _4300(4300, ""),


    // 5xx 系统错误
    _500(500, "服务端异常");

    public final int code;
    public final String msg;

    BizCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取消息格式化的ResultVO
     * @param args
     * @return
     */
    public ResultVO<Void> getResultVO(Object... args) {
        return ResultVO.ok(this.code, format2String(args), null);
    }

    public ResultVO<Void> getResultVO() {
        return ResultVO.ok(this.code, this.msg, null);
    }

    public TaxPreferenceException exception() {
        TaxPreferenceException bizException = new TaxPreferenceException(this.msg);
        bizException.setCode(this.code);
        return bizException;
    }

    public TaxPreferenceException exception(Object ...args) {
        TaxPreferenceException bizException = new TaxPreferenceException(format2String(args));
        bizException.setCode(this.code);
        return bizException;
    }

    private String format2String(Object... args) {
        FormattingTuple format = MessageFormatter.arrayFormat(this.msg, args);
        return format.getMessage();
    }
}
