package cn.huacloud.taxpreference.common.enums.user;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 渠道用户类型
 * @author wangkh
 */
public enum ChannelUserType implements IEnum<String> {
    XFT_OPEN_API("薪福通OpenAPI"),
    WORK_WEI_XIN("企业微信"),
    WEI_XIN("微信");

    ChannelUserType(String name) {
        this.name = name;
    }

    public final String name;

    @Override
    public String getValue() {
        return this.name();
    }
}
