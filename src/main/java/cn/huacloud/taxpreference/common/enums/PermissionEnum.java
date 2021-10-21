package cn.huacloud.taxpreference.common.enums;

/**
 * 权限枚举类
 * 统一汇聚接口权限码值
 * @author wangkh
 */
public enum PermissionEnum {

    // 政策法规

    // 政策解读

    // 税收优惠事项

    // 内容审核列表

    // 用户管理

    // 角色管理

    // 测试
    PERMISSION_TEST("permission_test", "", PermissionGroup.USER_MANAGE)
    ;

    public static final String testCode = "test";

    /**
     * 权限码值
     */
    public final String code;
    /**
     * 权限名称
     */
    public final String name;

    public final PermissionGroup permissionGroup;

    PermissionEnum(String code, String name, PermissionGroup permissionGroup) {
        this.code = code;
        this.name = name;
        this.permissionGroup = permissionGroup;
    }
}
