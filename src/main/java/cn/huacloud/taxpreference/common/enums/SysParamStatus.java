package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author wangkh
 */
public enum  SysParamStatus implements IEnum<String> {
    VALID("有效");

    public final String name;

    SysParamStatus(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
