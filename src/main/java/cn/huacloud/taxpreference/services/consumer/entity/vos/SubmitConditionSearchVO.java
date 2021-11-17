package cn.huacloud.taxpreference.services.consumer.entity.vos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
@Getter
@Setter
public class SubmitConditionSearchVO {
    /**
     * 条件名称
     */
    private String conditionName;

    /**
     * 具体要求
     */
    private String requirement;
}
