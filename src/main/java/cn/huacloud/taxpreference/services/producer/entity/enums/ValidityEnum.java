package cn.huacloud.taxpreference.services.producer.entity.enums;


import cn.huacloud.taxpreference.common.enums.SysCodeGetter;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * 有效性状态
 *
 * @author wuxin
 */
public enum ValidityEnum implements IEnum<String>, SysCodeGetter {

    /**
     * FULL_TEXT_VALID 全文有效
     * FULL_TEXT_REPEAL 全文废止
     * PARTIAL_REPEAL 部分废止
     */


    INVALID("失效"),
    FULL_TEXT_VALID("全文有效"),
    FULL_TEXT_REPEAL("全文废止"),
    PARTIAL_VALID("部分有效");

    @Getter
    private final String name;

    ValidityEnum(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
