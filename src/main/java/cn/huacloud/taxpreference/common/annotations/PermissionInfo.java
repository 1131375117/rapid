package cn.huacloud.taxpreference.common.annotations;

import cn.huacloud.taxpreference.common.enums.PermissionGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限信息补充
 * 补充 @SaCheckPermission 不具备的额外信息，方便权限信息自动入库
 * @author wangkh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionInfo {
    /**
     * 权限名称
     * @return 权限中文名称
     */
    String name();

    /**
     * 权限分组
     * @return
     */
    PermissionGroup group();
}
