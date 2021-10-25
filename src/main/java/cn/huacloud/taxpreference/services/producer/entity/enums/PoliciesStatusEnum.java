package cn.huacloud.taxpreference.services.producer.entity.enums;


import lombok.Getter;
import lombok.Setter;

/**
 * @author wuxin
 */
@Getter
public enum PoliciesStatusEnum {

    /**
     * OK 全文有效
     * ALL 全文废止
     * PART 部分废止
     */
    OK(0, ""),
    ALL(1, "全文废止"),
    PART(2, "部分废止");

    Integer val;

    String policiesStatus;

    PoliciesStatusEnum(Integer val, String policiesStatus) {
        this.val = val;
        this.policiesStatus = policiesStatus;
    }

}
