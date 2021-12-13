package cn.huacloud.taxpreference.services.message;

import cn.huacloud.taxpreference.common.enums.SmsBiz;

import java.util.Collections;
import java.util.List;

/**
 * 短信服务
 *
 * @author wangkh
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param phoneNumber 电话号码
     * @param smsBiz      短信业务
     */
    default void sendSms(String phoneNumber, SmsBiz smsBiz) {
        sendSms(Collections.singletonList(phoneNumber), smsBiz);
    }

    /**
     * 发送短信
     *
     * @param phoneNumbers 电话号码集合
     * @param smsBiz      短信业务
     */
    void sendSms(List<String> phoneNumbers, SmsBiz smsBiz);

    /**
     * 短信发送拦截器
     */
    interface Interceptor {
        /**
         * 短信发送拦截器
         * @param phoneNumbers 电话号码集合
         * @param smsBiz 短信业务
         */
        void apply(List<String> phoneNumbers, SmsBiz smsBiz);
    }
}
