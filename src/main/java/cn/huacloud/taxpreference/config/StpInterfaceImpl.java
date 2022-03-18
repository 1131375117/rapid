package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.huacloud.taxpreference.common.utils.ProducerUserUtil;
import cn.huacloud.taxpreference.services.user.entity.vos.ProducerLoginUserVO;
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
        if (StpUtil.getLoginType().equals(loginType)) {
            ProducerLoginUserVO loginUserVO = ProducerUserUtil.getCurrentUser();
            return loginUserVO.getPermissionCodes();
        }
        throw new UnsupportedOperationException("不支持此类权限控制，loginType：" + loginType);
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        if (StpUtil.getLoginType().equals(loginType)) {
            ProducerLoginUserVO loginUserVO = ProducerUserUtil.getCurrentUser();
            return loginUserVO.getRoleCodes();
        }
        throw new UnsupportedOperationException("不支持此类角色控制，loginType：" + loginType);
    }
}
