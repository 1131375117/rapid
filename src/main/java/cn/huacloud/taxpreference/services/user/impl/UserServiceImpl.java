package cn.huacloud.taxpreference.services.user.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.UserType;
import cn.huacloud.taxpreference.services.user.RoleService;
import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.ProducerUserDO;
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
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional
    @Override
    public void saveProducerUser(ProducerUserVO producerUserVO) {
        // 保存基本用户信息
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(producerUserVO, userDO);

        LocalDateTime now = LocalDateTime.now();

        // 密码MD5加密
        userDO.setPassword(SaSecureUtil.md5(producerUserVO.getPassword()))
                .setUserType(UserType.PRODUCER_USER)
                .setCreateTime(now)
                .setDisable(false)
                .setDeleted(false);
        // 执行保存
        userMapper.insert(userDO);
        // 用户ID回写
        producerUserVO.setId(userDO.getId());
        // 擦除密码
        producerUserVO.setPassword(null);

        // 保存后台用户信息
        ProducerUserDO producerUserDO = new ProducerUserDO();
        BeanUtils.copyProperties(producerUserVO, producerUserDO);
        producerUserDO.setUserId(userDO.getId())
                .setCreateTime(now)
                .setDeleted(false);
        // 执行保存
        producerUserMapper.insert(producerUserDO);
    }

    @Transactional
    @Override
    public void updateProducerUser(ProducerUserVO producerUserVO) {
        // save user
        UserDO userDO = userMapper.selectById(producerUserVO.getId());
        // validate
        if (userDO == null) {
            throw BizCode._4100.exception();
        }
        BeanUtils.copyProperties(producerUserVO, userDO);
        // execute save
        userMapper.updateById(userDO);

        // save producer user
        ProducerUserDO producerUserDO = producerUserMapper.selectByUserId(producerUserVO.getId());
        // validate
        if (producerUserDO == null) {
            throw BizCode._4100.exception();
        }
        BeanUtils.copyProperties(producerUserVO, producerUserDO);
        // execute save
        producerUserMapper.updateById(producerUserDO);
        // 擦除密码
        producerUserVO.setPassword(null);
    }

    @Override
    public ProducerUserVO getProducerUserByUserId(Long userId) {
        UserDO userDO = userMapper.selectById(userId);
        // validate
        if (userDO == null) {
            throw BizCode._4100.exception();
        }
        ProducerUserDO producerUserDO = producerUserMapper.selectByUserId(userId);
        // 属性拷贝
        ProducerUserVO producerUserVO = new ProducerUserVO();
        BeanUtils.copyProperties(producerUserDO, producerUserVO);
        BeanUtils.copyProperties(userDO, producerUserVO);
        producerUserVO.setId(userId);
        // 擦除密码
        producerUserVO.setPassword(null);

        return producerUserVO;
    }

    @Transactional
    @Override
    public Boolean switchDisableProducerUser(Long userId) {
        UserDO userDO = userMapper.selectById(userId);
        // validate
        if (userDO == null) {
            throw BizCode._4100.exception();
        }
        userDO.setDisable(!userDO.getDisable());
        userMapper.updateById(userDO);
        return userDO.getDisable();
    }

    @Override
    public void removeProducerUser(Long userId) {
        UserDO userDO = userMapper.selectById(userId);
        userDO.setDeleted(true);
        userMapper.updateById(userDO);
    }

    @Override
    public void setRoleToUser(String userId, List<String> roleCodes) {
        UserDO userDO = userMapper.selectById(userId);
        // validate
        if (userDO == null) {
            throw BizCode._4100.exception();
        }

        Set<String> allRoleCodes = roleService.getAllRoleCodes();
        Set<String> setRoleCodes = new HashSet<>(roleCodes);

        // 取角色码值交集
        setRoleCodes.retainAll(allRoleCodes);

        String roleCodesStr = String.join(",", setRoleCodes);
        userDO.setRoleCodes(roleCodesStr);

        // 保存更新
        userMapper.updateById(userDO);
    }
}
