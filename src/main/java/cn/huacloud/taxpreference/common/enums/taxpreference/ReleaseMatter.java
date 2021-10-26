package cn.huacloud.taxpreference.common.enums.taxpreference;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * @author fuhua
 */

public enum ReleaseMatter implements IEnum<String> {
    //是否属于我发布的事项
    ALL,
    MY_RELEASED,
    MY_UNRELEASED
    ;

    @Override
    public String getValue() {
        return this.name();
    }
}
