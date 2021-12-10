package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.services.user.entity.vos.ConsumerLoginUserVO;

/**
 * 消费者用户服务
 * @author wangkh
 */
public interface ConsumerUserService {

    /**
     * 获取用户登录视图
     * @param account 账户：用户账户、手机号、邮箱
     * @return 用户登录视图
     */
    default ConsumerLoginUserVO getLoginUserVO(String account) {
        ConsumerLoginUserVO loginUserVO = getLoginUserVOWithPassword(account);
        loginUserVO.setPassword(null);
        return loginUserVO;
    }


    /**
     * 获取用户登录视图
     * @param account 账户：用户账户、手机号、邮箱
     * @return 用户登录视图
     */
    ConsumerLoginUserVO getLoginUserVOWithPassword(String account);

    /**
     * 用户自动注册
     */
    void autoCreateUserByPhoneNumber(String phoneNumber);

    /**
     * 用户手动注册
     * @param phoneNumber 手机号
     * @param password 密码
     */
    void manualCreateUser(String phoneNumber, String password);

    /**
     * 更新用户密码
     * @param phoneNumber 手机号
     * @param password 密码
     */
    void retrievePassword(String phoneNumber, String password);

    /**
     * 检查手号机是否已经注册
     */

    /**
     * 检查用户账户、手机、邮箱是否已经注册
     */

    /**
     * 根据手机号用户登录信息
     */

    /**
     * 根据用户账户、手机号、邮箱获取用户登录信息
     */
}
