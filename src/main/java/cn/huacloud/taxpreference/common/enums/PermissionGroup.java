package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 权限分组枚举
 *
 * @author wangkh
 */
public enum PermissionGroup implements IEnum<String> {
    // 政策法规
    POLICIES("政策法规"),
    // 政策解读
    POLICIES_EXPLAIN("政策解读"),
    // 热点问答
    FREQUENTLY_ASKED_QUESTION("热点问答"),
    // 税收优惠事项
    TAX_PREFERENCE("税收优惠事项"),
    //热门咨询
    HOT_CONSULTATION("热门咨询"),
    // 内容审核列表
    APPROVAL("内容审核列表"),
    // 用户管理
    USER_MANAGE("用户管理"),
    //前台用户管理
    CONSUMER_MANAGE("前台用户管理"),
    // 角色管理
    ROLE_MANAGE("角色管理");


    public final String name;

    PermissionGroup(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
