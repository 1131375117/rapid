package cn.huacloud.taxpreference.common.enums.taxpreference;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author hua-cloud
 */

public enum TaxStatus implements IEnum<String> {
    //审批状态
    SAVE("保存"),
    SUBMIT("提交")
    ;

    private final String name;
    TaxStatus(String name) {
        this.name=name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
