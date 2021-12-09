package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 收藏类型
 * @author wangkh
 */
public enum CollectionType implements IEnum<String>{
    //收藏类型
    POLICIES("POLICIES"),
    POLICIES_EXPLAIN("POLICIES_EXPLAIN"),
    FREQUENTLY_ASKED_QUESTION("FREQUENTLY_ASKED_QUESTION"),
    TAX_PREFERENCE("TAX_PREFERENCE"),
    CASE_ANALYSIS("CASE_ANALYSIS");

    /**
     * 名称
     */
    public final String name;

    CollectionType(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return name();
    }


}
