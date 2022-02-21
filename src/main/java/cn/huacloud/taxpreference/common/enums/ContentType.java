package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 机构类型
 *
 * @author wangkh
 */
public enum ContentType implements IEnum<String> {

    QUESTION("问题"),
    ANSWER("答复");

    public final String name;

    ContentType(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
