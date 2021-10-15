package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;

import java.util.List;

/**
 * 用户服务
 * @author wangkh
 */
public interface UserService {

    List<UserDO> userList();
}
