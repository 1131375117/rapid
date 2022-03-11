package cn.huacloud.taxpreference.common.constants;

/**
 * 系统参数类型常量
 * @author wangkh
 */
public interface SysParamTypes {
    // 操作记录查看详情
    String OPERATION_VIEWS = "operation.views";
    // 短信基础参数
    String SMS_BASE = "sms.base";
    // 短信登录
    String SMS_LOGIN_VERIFICATION_CODE = "sms.login.verificationCode";
    // 短信注册
    String SMS_REGISTER_VERIFICATION_CODE = "sms.register.verificationCode";
    // 短信找回密码
    String SMS_RETRIEVE_PASSWORD_VERIFICATION_CODE = "sms.retrievePassword.verificationCode";
    // 短信专家回复
    String SMS_CONSULTATION_REPLY_CODE = "sms.consultation.replyCode";
    // 短信发送频率时间限制
    String SMS_RATE_LIMIT_SECONDS = "sms.rateLimitSeconds";
    // 短信验证码过期时间
    String SMS_VERIFICATION_CODE_EXPIRE_MINUTES = "sms.verificationCodeExpireMinutes";
    // 所属专题
    String POLICIES_SPECIAL_SUBJECT ="policies.specialSubject";
    // 税收优惠条件
    String TAX_PREFERENCE_CONDITION = "tax.preference.condition";
    //税务实务数据
    String TAX_CONSULTATION = "tax.consultation";
    //邮箱验证码过期时间
    String SES_VERIFICATION_CODE_EXPIRE_MINUTES = "ses.verificationCodeExpireMinutes";
    // 邮箱基础参数
    String SES_BASE = "ses.base";
    // 邮箱发送频率时间限制
    String SES_RATE_LIMIT_SECONDS = "ses.rateLimitSeconds";
    // 邮箱绑定
    String SES_BIND_VERIFICATION_CODE = "ses.bind.verificationCode";
    // 专家邮件回复
    String SES_CONSULTATION_REPLY = "ses.consultation.replyCode";
}
