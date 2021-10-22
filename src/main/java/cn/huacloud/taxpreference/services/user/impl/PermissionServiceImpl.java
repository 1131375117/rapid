package cn.huacloud.taxpreference.services.user.impl;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.huacloud.taxpreference.common.annotations.PermissionInfo;
import cn.huacloud.taxpreference.common.exception.MissingPermissionInfoException;
import cn.huacloud.taxpreference.services.user.PermissionService;
import cn.huacloud.taxpreference.services.user.entity.dos.PermissionDO;
import cn.huacloud.taxpreference.services.user.mapper.PermissionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限服务
 *
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PermissionServiceImpl implements PermissionService, CommandLineRunner {

    private final PermissionMapper permissionMapper;

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 扫描 HandlerMethod 中的注解权限信息，并保存到数据库
     */
    private void scanPermissionAndSave() {
        // 获取所有的 HandlerMethod
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();

        List<PermissionDO> permissionDOList = handlerMethods.values().stream()
                .map(handlerMethod -> {
                    // 获取 sa-token 的权限注解
                    SaCheckPermission saCheckPermission = handlerMethod.getMethodAnnotation(SaCheckPermission.class);
                    if (saCheckPermission == null) {
                        return null;
                    }
                    // 获取权限补充信息注解
                    PermissionInfo permissionInfo = handlerMethod.getMethodAnnotation(PermissionInfo.class);
                    if (permissionInfo == null) {
                        String handlerMethodName = handlerMethod.getMethod().toGenericString();
                        log.info("Controller方法 {} 添加了@SaCheckPermission注解, 但并未添加@PermissionInfo注解", handlerMethodName);
                        throw new MissingPermissionInfoException("Missing handler method name: " + handlerMethodName);
                    }
                    PermissionDO permissionDO = new PermissionDO();
                    permissionDO.setPermissionCode(saCheckPermission.value()[0]);
                    permissionDO.setPermissionName(permissionInfo.name());
                    permissionDO.setPermissionGroup(permissionInfo.group());
                    return permissionDO;
                }).filter(Objects::nonNull)
                // 根据权限码值进行排序
                .sorted(Comparator.comparing(PermissionDO::getPermissionCode))
                .collect(Collectors.toList());

        // 删除所有权限数据
        permissionMapper.delete(null);

        // 插入权限数据
        Set<String> checkSet = new HashSet<>();
        long count = 0;
        for (PermissionDO permissionDO : permissionDOList) {
            // 检查是否有重名的权限码值
            String permissionCode = permissionDO.getPermissionCode();
            if (checkSet.contains(permissionCode)) {
                log.error("权限码值重复，权限信息：{}", permissionDO.toString());
                continue;
            }
            // 添加当前权限码值到 checkSet 中
            checkSet.add(permissionCode);
            // 设置ID防止ID不停的自增
            permissionDO.setId(++count);
            // 保存权限
            permissionMapper.insert(permissionDO);
        }
        log.info("权限数据更新成功，共计{}个权限", count);
    }

    @Override
    public void run(String... args) {
        // 扫描权限并保存到数据库
        scanPermissionAndSave();
    }
}
