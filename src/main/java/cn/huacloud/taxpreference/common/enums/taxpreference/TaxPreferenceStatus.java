package cn.huacloud.taxpreference.common.enums.taxpreference;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 审批状态
 * @author fuhua
 */
public enum TaxPreferenceStatus implements IEnum<String> {
    //审批状态
    RELEASED("已发布"),
    UNRELEASED("待发布")
    ;

    private final String name;
    TaxPreferenceStatus(String name) {
        this.name=name;
    }

    @Override
    public String getValue() {
        return this.name;
    }
}