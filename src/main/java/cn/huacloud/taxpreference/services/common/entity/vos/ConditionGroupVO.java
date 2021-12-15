package cn.huacloud.taxpreference.services.common.entity.vos;

import lombok.Data;

import java.util.List;

/**
 * 税收优惠条件分组视图
 * @author wangkh
 */
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
    /**
     * 成员
     */
    private List<ConditionGroupVO> list;
}
