package cn.huacloud.taxpreference.services.common;

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
     * @param params      参数list
     */
    default void sendSms(String phoneNumber, SmsBiz smsBiz, List<String> params) {
        sendSms(Collections.singletonList(phoneNumber), smsBiz, params);
    }

    /**
     * 发送短信
     *
     * @param phoneNumbers 电话号码集合
     * @param smsBiz      短信业务
     * @param params      参数list
     */
    void sendSms(List<String> phoneNumbers, SmsBiz smsBiz, List<String> params);
}
