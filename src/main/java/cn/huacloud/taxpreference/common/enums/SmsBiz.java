package cn.huacloud.taxpreference.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * 短信业务枚举
 * @author wangkh
 */
public enum SmsBiz implements SysParamTypesGetter {

    SEND_LOGIN_VERIFICATION_CODE("发送登录验证码", Arrays.asList("", ""));
    /**
     * 系统码值类型
     */

    /**
     * 业务中文名称
     */
    public final String bizName;
    /**
     * 用于获取系统参数
     */
    @Getter
    public final List<String> sysParamTypes;

    SmsBiz(String bizName, List<String> sysParamTypes) {
        this.bizName = bizName;
        this.sysParamTypes = sysParamTypes;
    }
}
