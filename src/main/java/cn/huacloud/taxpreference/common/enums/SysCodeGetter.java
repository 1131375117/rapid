package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;

/**
 * 枚举获取系统码值获取器
 * 用来把枚举转换成系统码值视图，在ES中使用
 * @author wangkh
 */
public interface SysCodeGetter {

    /**
     * 获取枚举中文名称
     * @return 中文名称
     */
    String getName();

    /**
     * 获取枚举代码名称
     * @return 枚举代码名称
     */
    String name();

    /**
     * 获取枚举码值
     *
     * @return 获取枚举码值
     */
    default SysCodeSimpleVO getSysCode() {
        return new SysCodeSimpleVO().setCodeName(getName())
                .setCodeValue(name());
    }
}
