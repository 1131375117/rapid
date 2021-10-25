package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @description: 流程审批状态
 * @author: fuhua
 * @create: 2021-10-25 09:56
 **/
public enum ProcessStatus implements IEnum<String> {
    NOT_APPROVED("待通过"),
    UNDER_APPROVAL("审批中"),
    APPROVED("已通过");

    public final String name;

    ProcessStatus(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name;
    }
}
