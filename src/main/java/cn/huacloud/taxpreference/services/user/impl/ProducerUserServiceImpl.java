package cn.huacloud.taxpreference.services.user.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.common.constants.UserConstants;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.UserType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.user.ProducerUserService;
import cn.huacloud.taxpreference.services.user.RoleService;
import cn.huacloud.taxpreference.services.user.entity.dos.ProducerUserDO;
import cn.huacloud.taxpreference.services.user.entity.dos.RoleDO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserQueryDTO;
import cn.huacloud.taxpreference.services.user.entity.dtos.UserRoleAddDTO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserListVO;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerUserVO;
import cn.huacloud.taxpreference.services.user.entity.vos.RoleVO;
import cn.huacloud.taxpreference.services.user.mapper.ProducerUserMapper;
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
public class ProducerUserServiceImpl implements ProducerUserService {

    private final ProducerUserMapper producerUserMapper;

    private final RoleService roleService;

    @Override
    public ProducerUserDO getUserDOByUserAccount(String userAccount) {
        return producerUserMapper.getUserDOByAccount(userAccount);
    }

    @Override
    public ProducerLoginUserVO getLoginUserVOById(Long userId) {
        ProducerUserDO producerUserDO = producerUserMapper.selectById(userId);
        ProducerLoginUserVO loginUserVO = new ProducerLoginUserVO();
        BeanUtils.copyProperties(producerUserDO, loginUserVO);

        // 判断角色码值是否为空
        if (StringUtils.isBlank(producerUserDO.getRoleCodes())) {
            loginUserVO.setRoleCodes(new ArrayList<>());
            loginUserVO.setPermissionCodes(new ArrayList<>());
            return loginUserVO;
        }

        // 设置角色码值
        List<RoleDO> roleDOList = roleService.getRoleDOByRoleCodes(Arrays.asList(producerUserDO.getRoleCodes().split(",")));
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
    public PageVO<ProducerUserListVO> producerUserPageQuery(UserQueryDTO userQueryDTO) {
        String keyword = userQueryDTO.getKeyword();
        String userAccountKeyword = userQueryDTO.getUserAccountKeyword();
        String usernameKeyword = userQueryDTO.getUsernameKeyword();
        String roleCode = userQueryDTO.getRoleCode();
        String excludeRoleCode = userQueryDTO.getExcludeRoleCode();
        Boolean hiddenAdmin = userQueryDTO.getHiddenAdmin();
        // 构建查询条件
        LambdaQueryWrapper<ProducerUserDO> queryWrapper = Wrappers.lambdaQuery(ProducerUserDO.class)
                .eq(ProducerUserDO::getDeleted, false)
                .not(hiddenAdmin, w -> w.eq(ProducerUserDO::getUserAccount, UserConstants.ADMIN_USER_NAME))
                .and(keyword != null, i -> i.like(ProducerUserDO::getUserAccount, keyword).or().like(ProducerUserDO::getUsername, keyword))
                .like(userAccountKeyword != null, ProducerUserDO::getUserAccount, userAccountKeyword)
                .like(usernameKeyword != null, ProducerUserDO::getUsername, usernameKeyword)
                .apply(roleCode != null, "FIND_IN_SET ('" + roleCode + "', role_codes)")
                .apply(excludeRoleCode != null, "NOT FIND_IN_SET ('" + excludeRoleCode + "', role_codes)");
        // 执行查询
        IPage<ProducerUserDO> iPage = producerUserMapper.selectPage(userQueryDTO.createQueryPage(), queryWrapper);

        // 获取所有角色
        Map<String, RoleVO> allRoleVOMap = roleService.getAllRoleVOMap();
        // 数据映射
        List<ProducerUserListVO> records = iPage.getRecords().stream()
                .map(producerUserDO -> {
                    ProducerUserListVO userListVO = new ProducerUserListVO();
                    // 基础属性拷贝
                    BeanUtils.copyProperties(producerUserDO, userListVO);
                    // 设置角色
                    String roleCodes = producerUserDO.getRoleCodes();
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
        ProducerUserDO producerUserDO = new ProducerUserDO();
        BeanUtils.copyProperties(producerUserVO, producerUserDO);

        LocalDateTime now = LocalDateTime.now();

        // 密码MD5加密
        producerUserDO.setPassword(SaSecureUtil.md5(producerUserVO.getPassword()))
                .setCreateTime(now)
                .setDisable(false)
                .setDeleted(false);
        // 执行保存
        producerUserMapper.insert(producerUserDO);
        // 用户ID回写
        producerUserVO.setId(producerUserDO.getId());
        // 擦除密码
        producerUserVO.setPassword(null);

        log.info("添加后台用户成功，userAccount：{}", producerUserDO.getUserAccount());
    }

    @Transactional
    @Override
    public void updateProducerUser(ProducerUserVO producerUserVO) {
        // save user
        ProducerUserDO producerUserDO = producerUserMapper.selectById(producerUserVO.getId());
        // validate
        if (producerUserDO == null) {
            throw BizCode._4100.exception();
        }
        // 管理员账号名称不能被修改
        if (UserConstants.ADMIN_USER_NAME.equalsIgnoreCase(producerUserDO.getUserAccount())
                && !UserConstants.ADMIN_USER_NAME.equalsIgnoreCase(producerUserVO.getUserAccount())) {
            throw BizCode._4213.exception();
        }

        // 检查要修改的userAccount是否存在
        ProducerUserDO checkUser = producerUserMapper.getUserDOByAccountWithDelete(producerUserVO.getUserAccount());
        if (checkUser != null && !checkUser.getId().equals(producerUserVO.getId())) {
            throw BizCode._4212.exception();
        }

        CustomBeanUtil.copyProperties(producerUserVO, producerUserDO, true);

        if (producerUserVO.getPassword() != null) {
            // password md5
            producerUserDO.setPassword(SaSecureUtil.md5(producerUserVO.getPassword()));
        }

        // execute save
        producerUserMapper.updateById(producerUserDO);

        // 擦除密码
        producerUserVO.setPassword(null);
        log.info("更新后台用户信息成功，userAccount：{}", producerUserDO.getUserAccount());
    }

    @Override
    public ProducerUserVO getProducerUserByUserId(Long userId) {
        ProducerUserDO producerUserDO = producerUserMapper.selectById(userId);
        // validate
        if (producerUserDO == null) {
            throw BizCode._4100.exception();
        }
        ProducerUserVO producerUserVO = CustomBeanUtil.copyProperties(producerUserDO, ProducerUserVO.class);
        // 擦除密码
        producerUserVO.setPassword(null);
        return producerUserVO;
    }

    @Transactional
    @Override
    public Boolean switchDisableProducerUser(Long userId) {
        ProducerUserDO producerUserDO = producerUserMapper.selectById(userId);
        // validate
        if (producerUserDO == null) {
            throw BizCode._4100.exception();
        }
        // 检查是否为管理员用户
        adminUserCheck(producerUserDO.getUserAccount());

        producerUserDO.setDisable(!producerUserDO.getDisable());
        producerUserMapper.updateById(producerUserDO);
        // 禁用用户
        if (producerUserDO.getDisable()) {
            StpUtil.logout(userId);
        }
        log.info("切换用户禁用状态成功，userAccount：{}，disable：{}", producerUserDO.getUserAccount(), producerUserDO.getDisable());
        return producerUserDO.getDisable();
    }

    @Override
    public void removeProducerUser(Long userId) {
        ProducerUserDO producerUserDO = producerUserMapper.selectById(userId);
        // validate
        if (producerUserDO == null) {
            throw BizCode._4100.exception();
        }
        // 检查是否为管理员用户
        adminUserCheck(producerUserDO.getUserAccount());
        producerUserDO.setDeleted(true);
        producerUserMapper.updateById(producerUserDO);
    }

    @Override
    public void setRoleToUser(Long userId, List<String> roleCodes) {
        ProducerUserDO producerUserDO = producerUserMapper.selectById(userId);
        // validate
        if (producerUserDO == null) {
            throw BizCode._4100.exception();
        }

        // 检查是否为管理员用户
        adminUserCheck(producerUserDO.getUserAccount());

        Set<String> allRoleCodes = roleService.getAllRoleCodes(UserType.PRODUCER);
        Set<String> setRoleCodes = new HashSet<>(roleCodes);

        // 取角色码值交集
        setRoleCodes.retainAll(allRoleCodes);

        String roleCodesStr = String.join(",", setRoleCodes);
        producerUserDO.setRoleCodes(roleCodesStr);

        // 保存更新
        producerUserMapper.updateById(producerUserDO);

        log.info("修改用户拥有的角色成功， userAccount：{}，roleCodes：{}", producerUserDO.getUserAccount(), producerUserDO.getRoleCodes());

        // 设置权限后注销指定用户
        // StpUtil.logout(userId);
    }

    @Override
    public void removeUserRole(Long userId, String roleCode) {
        ProducerUserDO producerUserDO = producerUserMapper.selectById(userId);
        // 参数校验
        if (producerUserDO == null) {
            throw BizCode._4100.exception();
        }
        // 检查是否为管理员用户
        adminUserCheck(producerUserDO.getUserAccount());

        Set<String> roleCodeList = new HashSet<>(Arrays.asList(producerUserDO.getRoleCodes().split(",")));
        // 移除角色
        roleCodeList.remove(roleCode);
        String roleCodesStr = String.join(",", roleCodeList);
        producerUserDO.setRoleCodes(roleCodesStr);

        // 执行保存
        producerUserMapper.updateById(producerUserDO);
        log.info("移除后台用户角色成功， userAccount：{}， roleCode：{}", producerUserDO.getUserAccount(), roleCode);
    }

    @Override
    public boolean isUserAccountExist(String userAccount) {
        LambdaQueryWrapper<ProducerUserDO> queryWrapper = Wrappers.lambdaQuery(ProducerUserDO.class)
                .eq(ProducerUserDO::getUserAccount, userAccount);
        Long count = producerUserMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Transactional
    @Override
    public void addRoleToUser(List<UserRoleAddDTO> userRoleAddVOList) {

        Map<String, RoleVO> allRoleVOMap = roleService.getAllRoleVOMap();
        for (UserRoleAddDTO addDTO : userRoleAddVOList) {
            ProducerUserDO producerUserDO = producerUserMapper.selectById(addDTO.getUserId());
            // 用户校验
            if (producerUserDO == null) {
                throw BizCode._4100.exception();
            }

            // 跳过管理员
            if (UserConstants.ADMIN_USER_NAME.equalsIgnoreCase(producerUserDO.getUserAccount())) {
                continue;
            }

            // 权限校验
            if (!allRoleVOMap.containsKey(addDTO.getAddRoleCode())) {
                continue;
            }
            // 属性设置
            String roleCodes = separatorStrAddElement(producerUserDO.getRoleCodes(), addDTO.getAddRoleCode());
            producerUserDO.setRoleCodes(roleCodes);
            // 执行更新
            producerUserMapper.updateById(producerUserDO);
            log.info("为用户添加指定权限, userAccount: {} userName: {} roleCode: {}", producerUserDO.getUserAccount(), producerUserDO.getUsername(), addDTO.getAddRoleCode());
        }
    }

    /**
     * 检查是否为管理员用户
     *
     * @param userAccount 用户账户
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
