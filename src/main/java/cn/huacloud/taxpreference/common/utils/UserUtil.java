package cn.huacloud.taxpreference.common.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;

/**
 * @author wangkh
 */
public class UserUtil {

    // 登录用户 session 键名
    public static final String LOGIN_USER = "login_user";

    /**
     * 获取当前登录的用户信息
     * @return loginUserVO
     */
    public static LoginUserVO getCurrentUser() {
        return (LoginUserVO) StpUtil.getSession().get(LOGIN_USER);
    }

    /**
     * 获取当前的用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        LoginUserVO currentUser = getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return currentUser.getId();
    }

    /**
     * 获取当前的用户账户
     * @return 用户账户
     */
    public static String getCurrentUserAccount() {
        LoginUserVO currentUser = getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUserAccount();
    }
}
