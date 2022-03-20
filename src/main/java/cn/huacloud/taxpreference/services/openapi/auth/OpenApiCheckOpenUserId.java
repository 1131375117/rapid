package cn.huacloud.taxpreference.services.openapi.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 检查请求是否提供OpenUserId 请求头参数
 * @author wangkh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenApiCheckOpenUserId {
}
