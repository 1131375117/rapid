package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 用户类型
 * @author wangkh
 */
public enum UserType implements IEnum<String> {

    // 生产者用户
    PRODUCER_USER;

    @Override
    public String getValue() {
        return this.name();
    }
}
