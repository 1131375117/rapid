package cn.huacloud.taxpreference.services.user.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.UserType;
import cn.huacloud.taxpreference.services.user.RoleService;
import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserQueryDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserVO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleVO;
import cn.huacloud.taxpreference.services.user.entity.vos.UserListVO;
import cn.huacloud.taxpreference.services.user.mapper.ProducerUserMapper;
import cn.huacloud.taxpreference.services.user.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    private final ProducerUserMapper producerUserMapper;

    private final RoleService roleService;

    @Override
    public UserDO getUserDOByUserAccount(String userAccount) {
        return userMapper.getUserDOByAccount(userAccount);
    }

    @Override
    public LoginUserVO getLoginUserVOById(Long userId) {
        UserDO userDO = userMapper.selectById(userId);
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(userDO, loginUserVO);
        loginUserVO.setRoleCodes(Collections.singletonList("admin"));
        loginUserVO.setPermissionCodes(Collections.singletonList("*"));
        return loginUserVO;
    }

    @Override
    public PageVO<UserListVO> producerUserPageQuery(UserQueryDTO userQueryDTO) {
        String userAccountKeyword = userQueryDTO.getUserAccountKeyword();
        String usernameKeyword = userQueryDTO.getUsernameKeyword();
        String roleCode = userQueryDTO.getRoleCode();
        // 构建查询条件
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserType, UserType.PRODUCER_USER)
                .like(userAccountKeyword != null, UserDO::getUserAccount, userAccountKeyword)
                .like(usernameKeyword != null, UserDO::getUsername, usernameKeyword)
                .apply(roleCode != null, "FIND_IN_SET ('" + roleCode + "', roleCodes)");
        // 执行查询
        IPage<UserDO> iPage = userMapper.selectPage(userQueryDTO.createQueryPage(), queryWrapper);
        // 获取所有角色
        Map<String, RoleVO> allRoleVOMap = roleService.getAllRoleVOMap();
        // 数据映射
        List<UserListVO> records = iPage.getRecords().stream()
                .map(userDO -> {
                    UserListVO userListVO = new UserListVO();
                    // 基础属性拷贝
                    BeanUtils.copyProperties(userDO, userListVO);
                    // 设置角色
                    String roleCodes = userDO.getRoleCodes();
                    if (StringUtils.isNotBlank(roleCodes)) {
                        List<RoleVO> roleVOList = Arrays.stream(roleCodes.split(","))
                                .sorted()
                                .map(allRoleVOMap::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        userListVO.setRoles(roleVOList);
                    }
                    return userListVO;
                }).collect(Collectors.toList());
        // 返回新分页对象
        return PageVO.createPageVO(iPage, records);
    }

    @Override
    public void saveProducerUser(ProducerUserVO producerUserVO) {

    }
}
