package cn.huacloud.taxpreference.common.constants;

/**
 * 系统参数类型常量
 * @author wangkh
 */
public interface SysParamTypes {
    // 操作记录查看详情
    String OPERATION_VIEWS = "OPERATION_VIEWS";
    // 短信基础参数
    String SMS_BASE = "sms.base";
    // 短信登录
    String SMS_LOGIN_VERIFICATION_CODE = "sms.login.verificationCode";
    // 短信注册
    String SMS_REGISTER_VERIFICATION_CODE = "sms.login.verificationCode";
    // 短信找回密码
    String SMS_RETRIEVE_PASSWORD_VERIFICATION_CODE = "sms.login.verificationCode";
    // 短信发送频率时间限制
    String SMS_RATE_LIMIT_SECONDS = "sms.rateLimitSeconds";
    // 所属专题
    String POLICIES_SPECIAL_SUBJECT ="policies.specialSubject";
}
