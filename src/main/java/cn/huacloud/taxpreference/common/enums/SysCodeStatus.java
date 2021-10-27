package cn.huacloud.taxpreference.common.enums;

/**
 * 系统码值状态
 * @author wangkh
 */
public enum SysCodeStatus {

    VALID("有效"),
    DISABLE("禁用");

    public final String name;

    SysCodeStatus(String name) {
        this.name = name;
    }
}
