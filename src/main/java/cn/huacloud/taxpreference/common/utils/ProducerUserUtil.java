package cn.huacloud.taxpreference.common.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;

/**
 * 生产者用户工具类
 * 用于获取后台用户登录状态信息
 * @author wangkh
 */
public class ProducerUserUtil {

    // 登录用户 session 键名
    public static final String PRODUCER_USER = "producer_user";

    /**
     * 获取当前登录的后台用户信息
     * @return loginUserVO
     */
    public static ProducerLoginUserVO getCurrentUser() {
        return (ProducerLoginUserVO) StpUtil.getSession().get(PRODUCER_USER);
    }

    /**
     * 获取当前的后台用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        ProducerLoginUserVO currentUser = getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return currentUser.getId();
    }

    /**
     * 获取当前的后台用户账户
     * @return 用户账户
     */
    public static String getCurrentUserAccount() {
        ProducerLoginUserVO currentUser = getCurrentUser();
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUserAccount();
    }

    /**
     * 当前会话是否已经后台登录
     * @return 是否已登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }
}
