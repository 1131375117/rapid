package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.services.message.handler.SmsBizHandler;
import cn.huacloud.taxpreference.services.message.handler.SmsBizLoginVerificationCodeHandler;
import cn.huacloud.taxpreference.services.message.handler.SmsBizRegisterVerificationCodeHandler;
import cn.huacloud.taxpreference.services.message.handler.SmsBizRetrievePasswordVerificationCodeHandler;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 短信业务枚举
 * @author wangkh
 */
public enum SmsBiz implements SysParamTypesGetter, IEnum<String> {

    LOGIN_VERIFICATION_CODE("发送登录验证码", SmsBizLoginVerificationCodeHandler.class, Arrays.asList("sms.login.verificationCode", "sms.base")),
    REGISTER_VERIFICATION_CODE("发送注册验证码", SmsBizRegisterVerificationCodeHandler.class, Arrays.asList("sms.register.verificationCode", "sms.base")),
    RETRIEVE_PASSWORD_VERIFICATION_CODE("发送注册验证码", SmsBizRetrievePasswordVerificationCodeHandler.class, Arrays.asList("sms.retrievePassword.verificationCode", "sms.base"));
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

    @Override
    public String getValue() {
        return name();
    }
}
