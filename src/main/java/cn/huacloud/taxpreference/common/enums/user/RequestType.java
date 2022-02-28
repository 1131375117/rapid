package cn.huacloud.taxpreference.common.enums.user;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 消息类型
 *
 * @author wangkh
 */
public enum RequestType implements IEnum<String> {

    POST("POST"),
    GET("GET");
    public final String name;

    RequestType(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return name;
    }
}
