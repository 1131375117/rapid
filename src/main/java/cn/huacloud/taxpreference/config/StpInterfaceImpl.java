package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.huacloud.taxpreference.common.utils.UserUtil;
import cn.huacloud.taxpreference.services.user.entity.vos.LoginUserVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口扩展
 * @author wangkh
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUserVO loginUserVO = UserUtil.getCurrentUser();
        return loginUserVO.getPermissionCodes();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUserVO loginUserVO = UserUtil.getCurrentUser();
        return loginUserVO.getRoleCodes();
    }
}
