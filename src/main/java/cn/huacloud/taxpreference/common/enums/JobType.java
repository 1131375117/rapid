package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 系统码值状态
 * @author wangkh
 */
public enum JobType implements IEnum<String> {

    INSERT("插入"),
    UPDATE("修改");

    public final String name;

    JobType(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
