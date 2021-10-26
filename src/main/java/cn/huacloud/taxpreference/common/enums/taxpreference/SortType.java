package cn.huacloud.taxpreference.common.enums.taxpreference;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum SortType implements IEnum<String> {
    //排序字段
    CREATE_TIME("create_time"),
    UPDATE_TIME("update_time");

    private final String name;

    SortType(String name) {
        this.name=name;
    }

    @Override
    public String getValue() {
        return this.name;
    }


}
