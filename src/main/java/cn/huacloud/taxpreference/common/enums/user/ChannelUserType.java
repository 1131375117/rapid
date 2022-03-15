package cn.huacloud.taxpreference.common.enums.user;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 渠道用户类型
 * @author wangkh
 */
public enum ChannelUserType implements IEnum<String> {
    XFT_OPEN_API("薪福通OpenAPI", "薪福通用户"),
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
