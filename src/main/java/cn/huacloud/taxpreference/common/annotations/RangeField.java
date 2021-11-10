package cn.huacloud.taxpreference.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ES范围字段
 * @author wangkh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RangeField {

    /**
     * ES字段名称
     * 例如：releaseDate
     * @return ES字段名称
     */
    String value();
}
