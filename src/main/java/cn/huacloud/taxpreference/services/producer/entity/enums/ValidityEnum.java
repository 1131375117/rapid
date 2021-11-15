package cn.huacloud.taxpreference.services.producer.entity.enums;


import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 有效性状态
 *
 * @author wuxin
 */
public enum ValidityEnum implements IEnum<String> {

    /**
     * FULL_TEXT_VALID 全文有效
     * FULL_TEXT_REPEAL 全文废止
     * PARTIAL_REPEAL 部分废止
     */


            INVALID("失效"),
    FULL_TEXT_VALID("全文有效"),
    FULL_TEXT_REPEAL("全文废止"),
    PARTIAL_VALID("部分有效"),
    CLAUSE_INVALIDITY("条款失效"),
    FULL_TEXT_INVALIDATION("全文失效"),
    PARTIAL_REPEAL("部分废止"),
    UNKNOWN("未知");

    private final String name;

    ValidityEnum(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
