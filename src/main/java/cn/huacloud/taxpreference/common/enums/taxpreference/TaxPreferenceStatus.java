package cn.huacloud.taxpreference.common.enums.taxpreference;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 审批状态
 * @author fuhua
 */
public enum TaxPreferenceStatus implements IEnum<String> {

    RELEASED("已发布"),
    UNRELEASED("未发布")
    ;

    private final String name;
    TaxPreferenceStatus(String name) {
        this.name=name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
