package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.services.message.handler.*;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 邮件业务枚举
 * @author wangkh
 */
public enum EmailBiz implements SysParamTypesGetter, IEnum<String> {
    BIND_VERIFICATION_CODE("发送绑定验证码", EmailBizBindVerificationCodeHandler.class, Arrays.asList(SysParamTypes.SES_BIND_VERIFICATION_CODE, SysParamTypes.SES_BASE));

    /**
     * 业务中文名称
     */
    public final String bizName;

    /**
     * 短信具体业务发送处理器
     */
    public final Class<? extends EmailBizHandler> emailBizHandlerClass;

    /**
     * 用于获取系统参数
     */
    @Getter
    public final List<String> sysParamTypes;

    EmailBiz(String bizName, Class<? extends EmailBizHandler> emailBizHandlerClass, List<String> sysParamTypes) {
        this.bizName = bizName;
        this.emailBizHandlerClass = emailBizHandlerClass;
        this.sysParamTypes = sysParamTypes;
    }


    @Override
    public String getValue() {
        return name();
    }

}
