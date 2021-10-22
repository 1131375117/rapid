package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserQueryDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserVO;
import cn.huacloud.taxpreference.services.user.entity.vos.UserListVO;

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
     * 查询用户列表
     * @param userQueryDTO 用户信息查询类
     * @return 用户分页数据
     */
    PageVO<UserListVO> producerUserPageQuery(UserQueryDTO userQueryDTO);

    /**
     * 新增后台用户
     * @param producerUserVO
     */
    void saveProducerUser(ProducerUserVO producerUserVO);
}
