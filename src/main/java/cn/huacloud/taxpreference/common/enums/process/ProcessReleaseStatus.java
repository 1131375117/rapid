package cn.huacloud.taxpreference.common.enums.process;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @description: 流程是否发布
 * @author: fuhua
 * @create: 2021-10-25 09:56
 **/
public enum ProcessReleaseStatus implements IEnum<String> {
    PUBLISHED("已发布"),
    UNPUBLISHED("未发布");
    public final String name;

    ProcessReleaseStatus(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
