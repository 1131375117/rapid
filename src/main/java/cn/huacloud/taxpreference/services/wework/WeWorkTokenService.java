package cn.huacloud.taxpreference.services.wework;

import cn.huacloud.taxpreference.services.wework.entity.dtos.UserInfo3rdDTO;

/**
 * @author wangkh
 */
public interface WeWorkTokenService {
    void setSuiteTicket(String appName, String suiteTicket);

    String getSuiteTicket(String appName);

    String getSuiteToken(String appName);

    UserInfo3rdDTO getUserInfo3rdDTO(String appName, String code);
}
