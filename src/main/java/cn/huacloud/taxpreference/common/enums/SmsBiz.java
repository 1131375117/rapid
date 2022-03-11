package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.services.message.handler.*;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 短信业务枚举
 * @author wangkh
 */
public enum SmsBiz implements SysParamTypesGetter, IEnum<String> {

    LOGIN_VERIFICATION_CODE("发送登录验证码", SmsBizLoginVerificationCodeHandler.class, Arrays.asList(SysParamTypes.SMS_LOGIN_VERIFICATION_CODE, SysParamTypes.SMS_BASE)),
    REGISTER_VERIFICATION_CODE("发送注册验证码", SmsBizRegisterVerificationCodeHandler.class, Arrays.asList(SysParamTypes.SMS_REGISTER_VERIFICATION_CODE, SysParamTypes.SMS_BASE)),
    RETRIEVE_PASSWORD_VERIFICATION_CODE("发送密码重置验证码", SmsBizRetrievePasswordVerificationCodeHandler.class, Arrays.asList(SysParamTypes.SMS_RETRIEVE_PASSWORD_VERIFICATION_CODE, SysParamTypes.SMS_BASE)),
    CONSUMER_SUBSCRIBE__CODE("发送订阅业务", SmsBizRetrievePasswordVerificationCodeHandler.class, Arrays.asList(SysParamTypes.SMS_RETRIEVE_PASSWORD_VERIFICATION_CODE, SysParamTypes.SMS_BASE)),
    CONSULTATION_REPLAY("专家回复业务", SmsBizConsultationSubscribeCodeHandler.class, Arrays.asList(SysParamTypes.SMS_CONSULTATION_REPLY_CODE, SysParamTypes.SMS_BASE));;
    /**
     * 业务中文名称
     */
    public final String bizName;

    /**
     * 短信具体业务发送处理器
     */
    public final Class<? extends SmsBizHandler> smsBizHandlerClass;

    /**
     * 用于获取系统参数
     */
    @Getter
    public final List<String> sysParamTypes;

    SmsBiz(String bizName, Class<? extends SmsBizHandler> smsBizHandlerClass, List<String> sysParamTypes) {
        this.bizName = bizName;
        this.smsBizHandlerClass = smsBizHandlerClass;
        this.sysParamTypes = sysParamTypes;
    }

    @Override
    public String getValue() {
        return name();
    }
}
