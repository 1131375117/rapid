package cn.huacloud.taxpreference.common.enums.taxpreference;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @description: 税收优惠事项有效性
 * @author: fuhua
 * @create: 2021-10-25 10:11
 **/
public enum PreferenceValidation implements IEnum<String> {
    //税收优惠是否有效
    EFFECTIVE("有效"),
    INVALID("废止");

    public final String name;

    PreferenceValidation(String name) {
        this.name=name;
    }

    @Override
    public String getValue() {
        return this.name;
    }
}
