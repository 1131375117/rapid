package cn.huacloud.taxpreference.services.message;

import cn.huacloud.taxpreference.common.enums.EmailBiz;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 邮件服务
 * @author wangkh
 */
public interface EmailService {

    /**
     * 邮件发送
     * @param destination 邮件发送地址
     * @param emailBiz 邮件业务
     * @param params 参数map
     */
    default void sendEmail(String destination, EmailBiz emailBiz, Map<String, String> params) {
        sendEmail(Collections.singletonList(destination), emailBiz, params);
    }

    /**
     * 邮件发送
     * @param destinations 邮件发送地址集合
     * @param emailBiz 邮件业务
     * @param params 参数map
     */
    void sendEmail(List<String> destinations, EmailBiz emailBiz, Map<String, String> params);

}
