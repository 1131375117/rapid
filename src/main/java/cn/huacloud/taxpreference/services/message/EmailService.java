package cn.huacloud.taxpreference.services.message;

import cn.huacloud.taxpreference.common.enums.EmailBiz;

import java.util.Collections;
import java.util.List;

/**
 * 邮件服务
 * @author wangkh
 */
public interface EmailService {

    /**
     * 邮件发送
     * @param destination 邮件发送地址
     * @param emailBiz 邮件业务
     */
    default void sendEmail(String destination, EmailBiz emailBiz) {
        sendEmail(Collections.singletonList(destination), emailBiz);
    }

    /**
     * 邮件发送
     * @param destinations 邮件发送地址集合
     * @param emailBiz 邮件业务
     */
    void sendEmail(List<String> destinations, EmailBiz emailBiz);

    /**
     * 短信发送拦截器
     */
    interface Interceptor {
        /**
         * 短信发送拦截器
         * @param emails 邮件集合
         * @param emailBiz 邮件业务
         */
        void apply(List<String> emails, EmailBiz emailBiz);
    }

}
