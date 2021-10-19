package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;

import java.util.List;

/**
 * 用户服务
 * @author wangkh
 */
public interface UserService {

    /**
     * 根据用户账户获取用户基础信息
     * @param userAccount 用户账户
     * @return userDO
     */
    UserDO getUserDOByUserAccount(String userAccount);

    /**
     * 根据用户ID取登录用户视图
     * @param userId 用户ID
     * @return loginUserVO
     */
    LoginUserVO getLoginUserVOById(Long userId);

    /**
     * 测试用方法
     * @return
     */
    List<UserDO> userList();

}
