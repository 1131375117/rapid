package cn.huacloud.taxpreference.common.enums.user;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 用户创建方式
 * @author wangkh
 */
public enum CreateWay implements IEnum<String> {

    // 电话号码自动创建
    PHONE_NUMBER_AUTO,
    // 手工注册
    MANUAL_REGISTER,
    // 渠道开放userId创建
    CHANNEL_OPEN_USER_ID_AUTO;

    @Override
    public String getValue() {
        return this.name();
    }
}
