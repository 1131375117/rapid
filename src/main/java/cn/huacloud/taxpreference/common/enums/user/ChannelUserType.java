package cn.huacloud.taxpreference.common.enums.user;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 渠道用户类型
 * @author wangkh
 */
public enum ChannelUserType implements IEnum<String> {
    OPEN_API("OpenAPI", "OpenAPI用户"),
    WORK_WEI_XIN("企业微信", "企业微信用户"),
    WEI_XIN("微信", "微信用户");

    ChannelUserType(String name, String defaultUsername) {
        this.name = name;
        this.defaultUsername = defaultUsername;
    }

    public final String name;

    public final String defaultUsername;

    @Override
    public String getValue() {
        return this.name();
    }
}
