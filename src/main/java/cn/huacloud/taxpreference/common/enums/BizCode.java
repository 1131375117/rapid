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

    // 42xx 生产者用户
    _4200(4200, "用户未登录"),
    // 无此角色
    _4201(4201, "{}"),
    // 无此权限
    _4202(4202, "{}"),
    _4203(4203, "账号被封禁：{}秒后解封"),
    _4204(4204, "用户名或密码不正确"),
    _4205(4205, "账号被禁用"),
    _4206(4206, "角色码值重复"),
    _4207(4207, "只能删除未被使用的角色"),
    _4208(4208, "系统管理员角色无法修改或删除"),
    _4209(4209, "系统管理员账号无法禁用或删除"),
    _4210(4210, "验证码不正确"),
    _4211(4211, "登状态已过期"),
    _4212(4212, "用户账户已存在"),
    _4213(4213, "管理员账号名称不能被修改"),

    // 43xx 生产者服务
    _4300(4300, ""),
    _4301(4301, "审批不通过必须填写备注信息"),
    _4302(4302, "税收优惠名称已存在"),
    _4303(4303, "上传文件不能为空"),
    _4304(4304, "政策法规不存在"),
    _4305(4305, "政策法规标题或文号已存在"),
    _4306(4306, "当前政策已关联税收优惠事项，请先删除优惠事项,在进行政策删除操作！"),
    _4307(4307, "当前政策解读为空！"),
    _4308(4308, "该政策法规已经被其他政策解读关联！"),
    _4309(4309, "税收优惠不存在"),


    _4310(4310, "未发布税收政策不能撤回!"),
    _4311(4311, "您操作的税收优惠包含已发布内容不能修改或删除!"),
    _4312(4312, "您操作的税收优惠包含正在审批中内容，请勿进行其他操作!"),
    _4313(4313, "您录入的标签字段与纳税人类型存在互斥、请检查后重新提交!"),


    // 44xx 公用服务
    _4400(4400, "指定的附件已经设置过docId，无法再次设置"),
    _4401(4401, "附件不存在"),
    _4402(4402, "系统密码不正确"),
    _4403(4403, "已有一个同步任务在执行，任务提交时间：{}"),
    _4404(4404, "短信验证码发送过于频繁，请于{}秒后再进行发送"),

    // 45xx 消费者服务
    _4500(4500, "找不到对应详情信息"),
    _4501(4501, "找不到对应操作类型"),

    // 46xxx 消费者用户
    _4600(4600, "短信验证码不正确"),
    _4601(4601, "手机号码已注册"),
    _4602(4602, "验证码不正确"),
    _4603(4603, "用户名或密码不正确"),
    _4604(4604, "密码加密不正确，无法解密"),


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
