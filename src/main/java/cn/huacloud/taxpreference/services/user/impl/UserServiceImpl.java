package cn.huacloud.taxpreference.services.user.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.common.constants.UserConstants;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.UserType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.user.RoleService;
import cn.huacloud.taxpreference.services.user.UserService;
import cn.huacloud.taxpreference.services.user.entity.dos.ProducerUserDO;
import cn.huacloud.taxpreference.services.user.entity.dos.RoleDO;
import cn.huacloud.taxpreference.services.user.entity.dos.UserDO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserQueryDTO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserRoleAddDTO;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户服务实现
 *
 * @author wangkh
 */
@Slf4j
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

        // 判断角色码值是否为空
        if (StringUtils.isBlank(userDO.getRoleCodes())) {
            loginUserVO.setRoleCodes(new ArrayList<>());
            loginUserVO.setPermissionCodes(new ArrayList<>());
            return loginUserVO;
        }

        // 设置角色码值
        List<RoleDO> roleDOList = roleService.getRoleDOByRoleCodes(Arrays.asList(userDO.getRoleCodes().split(",")));
        List<String> roleCodes = roleDOList.stream().map(RoleDO::getRoleCode).sorted().collect(Collectors.toList());
        loginUserVO.setRoleCodes(roleCodes);
        // 设置权限码值
        List<String> permissionCodes = roleDOList.stream().flatMap(roleDO -> {
            String permissionCodeStr = roleDO.getPermissionCodes();
            if (StringUtils.isBlank(permissionCodeStr)) {
                return Stream.empty();
            }
            return Arrays.stream(permissionCodeStr.split(","));
        }).sorted().collect(Collectors.toList());
        loginUserVO.setPermissionCodes(permissionCodes);

        return loginUserVO;
    }

    @Override
    public PageVO<UserListVO> producerUserPageQuery(UserQueryDTO userQueryDTO) {
        String keyword = userQueryDTO.getKeyword();
        String userAccountKeyword = userQueryDTO.getUserAccountKeyword();
        String usernameKeyword = userQueryDTO.getUsernameKeyword();
        String roleCode = userQueryDTO.getRoleCode();
        String excludeRoleCode = userQueryDTO.getExcludeRoleCode();
        Boolean hiddenAdmin = userQueryDTO.getHiddenAdmin();
        // 构建查询条件
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserType, UserType.PRODUCER_USER)
                .eq(UserDO::getDeleted, false)
                .not(hiddenAdmin, w -> w.eq(UserDO::getUserAccount, UserConstants.ADMIN_USER_NAME))
                .and(keyword != null, i -> i.like(UserDO::getUserAccount, keyword).or().like(UserDO::getUsername, keyword))
                .like(userAccountKeyword != null, UserDO::getUserAccount, userAccountKeyword)
                .like(usernameKeyword != null, UserDO::getUsername, usernameKeyword)
                .apply(roleCode != null, "FIND_IN_SET ('" + roleCode + "', role_codes)")
                .apply(excludeRoleCode != null, "NOT FIND_IN_SET ('" + excludeRoleCode + "', role_codes)");
        // 执行查询
        IPage<UserDO> iPage = userMapper.selectPage(userQueryDTO.createQueryPage(), queryWrapper);

        Map<Long, ProducerUserDO> producerUserDOMap = producerUserMapper.getMapByUserIds(iPage.getRecords().stream().map(UserDO::getId).collect(Collectors.toList()));
        // 获取所有角色
        Map<String, RoleVO> allRoleVOMap = roleService.getAllRoleVOMap();
        // 数据映射
        List<UserListVO> records = iPage.getRecords().stream()
                .map(userDO -> {
                    UserListVO userListVO = new UserListVO();
                    // 基础属性拷贝
                    BeanUtils.copyProperties(userDO, userListVO);
                    // 生产者属性拷贝
                    ProducerUserDO producerUserDO = producerUserDOMap.get(userDO.getId());
                    if (producerUserDO != null) {
                        userListVO.setPhoneNumber(producerUserDO.getPhoneNumber());
                    }
                    // 设置角色
                    String roleCodes = userDO.getRoleCodes();
                    if (StringUtils.isNotBlank(roleCodes)) {
                        List<RoleVO> roleVOList = Arrays.stream(roleCodes.split(","))
                                .sorted()
                                .map(allRoleVOMap::get)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        userListVO.setRoles(roleVOList);
                    } else {
                        userListVO.setRoles(new ArrayList<>());
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
        log.info("添加后台用户成功，userAccount：{}", userDO.getUserAccount());
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
        // 管理员账号名称不能被修改
        if (UserConstants.ADMIN_USER_NAME.equalsIgnoreCase(userDO.getUserAccount())
                && !UserConstants.ADMIN_USER_NAME.equalsIgnoreCase(producerUserVO.getUserAccount())) {
            throw BizCode._4213.exception();
        }

        // 检查要修改的userAccount是否存在
        UserDO checkUser = userMapper.getUserDOByAccountWithDelete(producerUserVO.getUserAccount());
        if (checkUser != null && !checkUser.getId().equals(producerUserVO.getId())) {
            throw BizCode._4212.exception();
        }

        CustomBeanUtil.copyProperties(producerUserVO, userDO);

        if (producerUserVO.getPassword() != null) {
            // password md5
            userDO.setPassword(SaSecureUtil.md5(producerUserVO.getPassword()));
        }

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
        log.info("更新后台用户信息成功，userAccount：{}", userDO.getUserAccount());
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
        if (producerUserDO != null) {
            BeanUtils.copyProperties(producerUserDO, producerUserVO);
        }
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
        // 检查是否为管理员用户
        adminUserCheck(userDO.getUserAccount());

        userDO.setDisable(!userDO.getDisable());
        userMapper.updateById(userDO);
        // 禁用用户
        if (userDO.getDisable()) {
            StpUtil.logout(userId);
        }
        log.info("切换用户禁用状态成功，userAccount：{}，disable：{}", userDO.getUserAccount(), userDO.getDisable());
        return userDO.getDisable();
    }

    @Override
    public void removeProducerUser(Long userId) {
        UserDO userDO = userMapper.selectById(userId);
        // validate
        if (userDO == null) {
            throw BizCode._4100.exception();
        }
        // 检查是否为管理员用户
        adminUserCheck(userDO.getUserAccount());
        userDO.setDeleted(true);
        userMapper.updateById(userDO);
    }

    @Override
    public void setRoleToUser(Long userId, List<String> roleCodes) {
        UserDO userDO = userMapper.selectById(userId);
        // validate
        if (userDO == null) {
            throw BizCode._4100.exception();
        }

        // 检查是否为管理员用户
        adminUserCheck(userDO.getUserAccount());

        Set<String> allRoleCodes = roleService.getAllRoleCodes();
        Set<String> setRoleCodes = new HashSet<>(roleCodes);

        // 取角色码值交集
        setRoleCodes.retainAll(allRoleCodes);

        String roleCodesStr = String.join(",", setRoleCodes);
        userDO.setRoleCodes(roleCodesStr);

        // 保存更新
        userMapper.updateById(userDO);

        log.info("修改用户拥有的角色成功， userAccount：{}，roleCodes：{}", userDO.getUserAccount(), userDO.getRoleCodes());

        // 设置权限后注销指定用户
        // StpUtil.logout(userId);
    }

    @Override
    public void removeUserRole(Long userId, String roleCode) {
        UserDO userDO = userMapper.selectById(userId);
        // 参数校验
        if (userDO == null) {
            throw BizCode._4100.exception();
        }
        // 检查是否为管理员用户
        adminUserCheck(userDO.getUserAccount());

        Set<String> roleCodeList = new HashSet<>(Arrays.asList(userDO.getRoleCodes().split(",")));
        // 移除角色
        roleCodeList.remove(roleCode);
        String roleCodesStr = String.join(",", roleCodeList);
        userDO.setRoleCodes(roleCodesStr);

        // 执行保存
        userMapper.updateById(userDO);
        log.info("移除后台用户角色成功， userAccount：{}， roleCode：{}", userDO.getUserAccount(), roleCode);
    }

    @Override
    public boolean isUserAccountExist(String userAccount) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUserAccount, userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Transactional
    @Override
    public void addRoleToUser(List<UserRoleAddDTO> userRoleAddVOList) {

        Map<String, RoleVO> allRoleVOMap = roleService.getAllRoleVOMap();
        for (UserRoleAddDTO addDTO : userRoleAddVOList) {
            UserDO userDO = userMapper.selectById(addDTO.getUserId());
            // 用户校验
            if (userDO == null) {
                throw BizCode._4100.exception();
            }

            // 跳过管理员
            if (UserConstants.ADMIN_USER_NAME.equalsIgnoreCase(userDO.getUserAccount())) {
                continue;
            }

            // 权限校验
            if (!allRoleVOMap.containsKey(addDTO.getAddRoleCode())) {
                continue;
            }
            // 属性设置
            String roleCodes = separatorStrAddElement(userDO.getRoleCodes(), addDTO.getAddRoleCode());
            userDO.setRoleCodes(roleCodes);
            // 执行更新
            userMapper.updateById(userDO);
            log.info("为用户添加指定权限, userAccount: {} userName: {} roleCode: {}", userDO.getUserAccount(), userDO.getUsername(), addDTO.getAddRoleCode());
        }
    }

    /**
     * 检查是否为管理员用户
     *
     * @param userAccount
     */
    private void adminUserCheck(String userAccount) {
        if (UserConstants.ADMIN_USER_NAME.equalsIgnoreCase(userAccount)) {
            throw BizCode._4209.exception();
        }
    }

    /**
     * 给","分隔的字符串添加新元素
     * @param target 目标字符串
     * @param element 新元素
     * @return 合并后的字符串
     */
    private static String separatorStrAddElement(String target, String element) {
        if (StringUtils.isBlank(target)) {
            return element;
        }
        List<String> targetList = Arrays.asList(target.split(","));
        Set<String> treeSet = new TreeSet<>(targetList);
        treeSet.add(element);
        return String.join(",", treeSet);
    }
}
