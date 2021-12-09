package cn.huacloud.taxpreference.common.utils;

import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;

/**
 * 消费者者用户工具类
 * 用于获取前台用户登录状态信息
 * @author wangkh
 */
public class ConsumerUerUtil {
    // 登录用户 session 键名
    public static final String CONSUMER_USER = "consumer_user";

    /**
     * 获取当前登录的后台用户信息
     * @return loginUserVO
     */
    public static ProducerLoginUserVO getCurrentUser() {
        return (ProducerLoginUserVO) ConsumerStpUtil.getSession().get(CONSUMER_USER);
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
        return ConsumerStpUtil.isLogin();
    }
}
