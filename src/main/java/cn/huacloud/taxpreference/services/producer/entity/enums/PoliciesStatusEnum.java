package cn.huacloud.taxpreference.services.producer.entity.enums;


import com.baomidou.mybatisplus.annotation.IEnum;


/**
 * 政策法规废止状态
 * @author wuxin
 */
public enum PoliciesStatusEnum implements IEnum<String> {

    /**
     * FULL_TEXT_REPEAL 全文废止
     * PARTIAL_REPEAL 部分废止
     */
    FULL_TEXT_REPEAL( "全文废止"),
    PARTIAL_REPEAL("部分废止");

    private final String policiesStatus;

    PoliciesStatusEnum(String policiesStatus) {
        this.policiesStatus = policiesStatus;
    }

    @Override
    public String getValue() {
        return this.policiesStatus;
    }
}
