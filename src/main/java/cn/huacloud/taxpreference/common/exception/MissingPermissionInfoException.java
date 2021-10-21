package cn.huacloud.taxpreference.common.exception;

/**
 * 缺少权限信息注解异常
 * 使用 @SaCheckPermission 注解的 Controller 方法必须 添加 @PermissionInfo 注解
 * @author wangkh
 */
public class MissingPermissionInfoException extends TaxPreferenceException {
    public MissingPermissionInfoException(String message) {
        super(message);
    }
}
