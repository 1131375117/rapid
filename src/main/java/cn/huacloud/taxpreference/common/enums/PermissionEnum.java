package cn.huacloud.taxpreference.common.enums;

/**
 * 权限枚举类
 * 统一汇聚接口权限码值
 * @author wangkh
 */
public enum PermissionEnum {

    ;

    /**
     * 权限码值
     */
    public final String code;
    /**
     * 权限名称
     */
    public final String name;

    PermissionEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
