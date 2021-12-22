package cn.huacloud.taxpreference.common.annotations;

import cn.huacloud.taxpreference.common.enums.SysCodeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否包含子节点码值
 * @author wangkh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WithChildrenCodes {
    /**
     * 码值类型
     * @return
     */
    SysCodeType value();
}
