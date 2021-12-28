package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 浏览类型
 * @author fuhua
 */
public enum ViewType implements IEnum<String>{
    //浏览类型
    POLICIES("views.policies"),
    POLICIES_EXPLAIN("views.policiesExplain"),
    FREQUENTLY_ASKED_QUESTION("views.frequentlyAskedQuestion"),
    TAX_PREFERENCE("views.taxPreference"),
    CASE_ANALYSIS("views.caseAnalysis");

    /**
     * 名称
     */
    public final String name;

    ViewType(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return name();
    }


}
