package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 消息类型
 * @author wangkh
 */
public enum MsgType implements IEnum<String> {
    // 短信
    SMS,
    // 邮件
    EMAIL;

    @Override
    public String getValue() {
        return name();
    }
}
