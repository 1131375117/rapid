package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 机构类型
 *
 * @author wangkh
 */
public enum OrganizationType implements IEnum<String> {
    //专业机构
    PROFESSIONAL("专业机构"),
    OFFICIAL("官方机关");

    public final String name;

    OrganizationType(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
