package cn.huacloud.taxpreference.services.producer.entity.enums;


import com.baomidou.mybatisplus.annotation.IEnum;


/**
 * 政策法规废止状态
 *
 * @author wuxin
 */
public enum PoliciesExplainStatusEnum implements IEnum<String> {

    /**
     * FULL_TEXT_VALID 全文有效
     * FULL_TEXT_REPEAL 全文废止
     * PARTIAL_REPEAL 部分废止
     */
    REPTILE_SYNCHRONIZATION("爬虫同步"),
    PUBLISHED("已发布");

    private final String name;

    PoliciesExplainStatusEnum(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
