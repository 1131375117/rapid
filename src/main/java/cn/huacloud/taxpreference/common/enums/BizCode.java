package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * 业务状态码值
 *
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
    _4205(4205, "账号被禁用"),
    _4206(4206, "角色码值重复"),
    _4207(4207, "只能删除未被使用的角色"),
    _4208(4208, "系统管理员角色无法修改或删除"),
    _4209(4209, "系统管理员账号无法禁用或删除"),
    _4210(4210, "验证码不正确"),
    _4211(4211, "登状态已过期"),

    // 43xx 生产者服务
    _4300(4300, ""),
    _4301(4301, "审批不通过必须填写备注信息"),
    _4302(4302, "税收优惠名称已存在"),
    _4303(4303, "上传文件不能为空"),
    _4304(4304, "政策法规不存在"),

    // 44xx 公用服务
    _4400(4400, "指定的附件已经设置过docId，无法再次设置"),
    _4401(4401, "附件不存在"),

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
     *
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

    public TaxPreferenceException exception(Object... args) {
        TaxPreferenceException bizException = new TaxPreferenceException(format2String(args));
        bizException.setCode(this.code);
        return bizException;
    }

    private String format2String(Object... args) {
        FormattingTuple format = MessageFormatter.arrayFormat(this.msg, args);
        return format.getMessage();
    }
}
