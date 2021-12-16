package cn.huacloud.taxpreference.services.common.entity.vos;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 税收优惠条件分组视图
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class ConditionGroupVO {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
}
