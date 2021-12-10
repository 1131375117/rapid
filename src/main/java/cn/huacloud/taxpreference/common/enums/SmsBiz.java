package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.services.message.handler.SmsBizHandler;
import cn.huacloud.taxpreference.services.message.handler.SmsBizLoginVerificationCodeHandler;
import cn.huacloud.taxpreference.services.message.handler.SmsBizRegisterVerificationCodeHandler;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 短信业务枚举
 * @author wangkh
 */
public enum SmsBiz implements SysParamTypesGetter {

    LOGIN_VERIFICATION_CODE("发送登录验证码", SmsBizLoginVerificationCodeHandler.class, Arrays.asList("sms.base", "sms.login.verificationCode")),
    register_VERIFICATION_CODE("发送注册验证码", SmsBizRegisterVerificationCodeHandler.class, Arrays.asList("sms.base", "sms.register.verificationCode"));
    /**
     * 系统码值类型
     */

    /**
     * 业务中文名称
     */
    public final String bizName;

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
}
