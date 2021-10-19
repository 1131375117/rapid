package cn.huacloud.taxpreference.common.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;

/**
 * @author wangkh
 */
public class UserUtil {

    // 登录用户 session 键名
    public static final String LOGIN_USER = "login_user";

    public static LoginUserVO getCurrentUser() {
        return (LoginUserVO) StpUtil.getSession().get(LOGIN_USER);
    }
}
