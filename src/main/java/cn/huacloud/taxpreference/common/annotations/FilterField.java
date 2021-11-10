package cn.huacloud.taxpreference.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ES过滤字段注解
 * @author wangkh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FilterField {

    /**
     * ES字段名称
     * 例如：title taxCategories.codeValue
     * @return ES字段名称
     */
    String value();
}
