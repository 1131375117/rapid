package cn.huacloud.taxpreference.common.annotations;

import cn.huacloud.taxpreference.common.enums.LimitType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流注解
 *
 * @author hua-cloud
 */

@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitApi {

    /**
     * 限制类型
     */
    LimitType limitType() default LimitType.LEAKY_BUCKET;

    /**
     * 每秒可处理请求数,exec_count_per_second
     */
    int execCountPerSecond() default 800;

    /** 限流过期时间，默认1秒，即每durationSeconds秒允许多少请求（当limit_type=1时有效）, 数据库字段：durationSeconds */
    int durationSeconds() default 1;
}