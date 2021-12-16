package cn.huacloud.taxpreference.common.annotations;

import cn.huacloud.taxpreference.common.enums.SysCodeType;

/**
 * 是否包含子节点码值
 * @author wangkh
 */
public @interface WithChildrenCodes {
    /**
     * 码值类型
     * @return
     */
    SysCodeType value();
}
