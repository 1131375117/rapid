package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 系统码值类型
 * @author wangkh
 */
public enum SysCodeType implements IEnum<String> {
    TAX_CATEGORIES("所属税种"),
    AREA("所属区域"),
    INDUSTRY("适用行业"),
    TAXPAYER_IDENTIFY_TYPE("纳税人资格认定类型"),
    ENTERPRISE_TYPE("适用企业类型"),
    TAXPAYER_REGISTER_TYPE("纳税人登记注册类型"),
    TAXPAYER_TYPE("纳税人类型");

    public final String name;

    SysCodeType(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
