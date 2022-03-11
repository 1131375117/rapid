package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 机构类型
 *
 * @author wangkh
 */
public enum RedPointStatus implements IEnum<String> {
    //专业机构
    SHOW("显示"),
    HIDDEN("隐藏");

    public final String name;

    RedPointStatus(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
