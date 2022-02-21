package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 机构类型
 *
 * @author wangkh
 */
public enum ReplyStatus implements IEnum<String> {
    //专业机构
    NOT_REPLY("未答复"),
    HAVE_REPLY("已答复");

    public final String name;

    ReplyStatus(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
