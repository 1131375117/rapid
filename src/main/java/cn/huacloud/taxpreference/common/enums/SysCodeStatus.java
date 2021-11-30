package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 系统码值状态
 * @author wangkh
 */
public enum SysCodeStatus implements IEnum<String> {

    VALID("有效"),
    HIDDEN("隐藏"),
    DISABLE("禁用");

    public final String name;

    SysCodeStatus(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
