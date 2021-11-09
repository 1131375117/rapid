package cn.huacloud.taxpreference.services.producer.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author wuxin
 */

public enum PoliciesSortType implements IEnum<String> {
    //排序字段
    RELEASE_DATE("release_date"),
    UPDATE_TIME("update_time");

    private final String name;

    PoliciesSortType(String name) {
        this.name=name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
