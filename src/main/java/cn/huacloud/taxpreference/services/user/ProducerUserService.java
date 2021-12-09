package cn.huacloud.taxpreference.services.user;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.user.entity.dos.ProducerUserDO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserQueryDTO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserRoleAddDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserListVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserVO;

import java.util.List;

/**
 * 生产者用户服务
 * @author wangkh
 */
public interface ProducerUserService {

    /**
     * 根据用户账户获取用户基础信息
     * @param userAccount 用户账户
     * @return producerUserDO
     */
    ProducerUserDO getUserDOByUserAccount(String userAccount);

    /**
     * 根据用户ID取登录用户视图
     * @param userId 用户ID
     * @return loginUserVO
     */
    ProducerLoginUserVO getLoginUserVOById(Long userId);

    /**
     * 查询用户列表
     * @param userQueryDTO 用户信息查询类
     * @return 用户分页数据
     */
    PageVO<ProducerUserListVO> producerUserPageQuery(UserQueryDTO userQueryDTO);

    /**
     * 新增后台用户
     * @param producerUserVO 后台用户信息视图
     */
    void saveProducerUser(ProducerUserVO producerUserVO);

    /**
     * 修改后台用户
     * @param producerUserVO 后台用户信息视图
     */
    void updateProducerUser(ProducerUserVO producerUserVO);

    /**
     * 根据ID查询后台用户详情
     * @param userId 用户ID
     * @return 后台用户信息视图
     */
    ProducerUserVO getProducerUserByUserId(Long userId);

    /**
     * 根据ID禁用/启用用户
     * @param userId 用户ID
     * @return 是否被禁用
     */
    Boolean switchDisableProducerUser(Long userId);

    /**
     * 删除后台用户
     * @param userId 用户ID
     */
    void removeProducerUser(Long userId);

    /**
     * 给指定ID用户赋予角色
     * @param userId 用户ID
     * @param roleCodes 角色码值
     */
    void setRoleToUser(Long userId, List<String> roleCodes);

    /**
     * 移除指定用户的指定角色
     * @param userId 用户ID
     * @param roleCode 角色码值
     */
    void removeUserRole(Long userId, String roleCode);

    /**
     *
     * @param userAccount 用户账户
     * @return 是否存在
     */
    boolean isUserAccountExist(String userAccount);

    /**
     * 批量为用户添加新的角色
     * @param userRoleAddVOList 用户角色添加视图
     */
    void addRoleToUser(List<UserRoleAddDTO> userRoleAddVOList);
}
