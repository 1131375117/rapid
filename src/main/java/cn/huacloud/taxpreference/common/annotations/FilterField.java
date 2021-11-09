package cn.huacloud.taxpreference.common.annotations;

/**
 * ES过滤字段注解
 * @author wangkh
 */
public @interface FilterField {

    /**
     * ES字段名称
     * 例如：title
     * @return
     */
    String value();
}
