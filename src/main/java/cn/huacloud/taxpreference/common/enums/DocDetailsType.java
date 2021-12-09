package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author fuhua
 **/
public enum DocDetailsType implements IEnum<String> {
    //操作详情
    TAX_PREFERENCE,
    POLICIES,
    OTHER_DOC,
    POLICIES_EXPLAIN,
    FREQUENTLY_ASKED_QUESTION;

    @Override
    public String getValue() {
        return this.name();
    }
}
