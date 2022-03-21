package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.services.user.entity.dos.ConsumerUserDO;
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
        if (loginUserVO != null) {
            loginUserVO.setPassword(null);
        }
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
     * @param phoneNumber 手机号
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
     * 修改用户名称
     * @param account 用户账号
     * @param username 用户名
     */
    void updateConsumerName(String account, String username);

    /**
     * 修改密码
     * @param phoneNumber 手机号
     * @param newPassword 新密码
     */
    void updatePassword(String phoneNumber, String newPassword );

    /**
     * 邮箱绑定
     * @param email 邮件名称
     * @param userAccount 用户账户
     */
    void bindEmail(String email, String userAccount);
    /**
     * 通过ID获取消费者用户实体
     * @param consumerUserId 消费者用户ID
     * @return ConsumerUserDO
     */
    ConsumerUserDO getUserDOById(Long consumerUserId);

    /**
     * 通过开放用户ID自动注册
     * @param username
     * @param openUserId 开放用户ID
     */
    ConsumerUserDO autoCreateUserByOpenUserId(String username, String openUserId);
}
