package cn.huacloud.taxpreference.services.common;

import cn.huacloud.taxpreference.common.enums.SmsBiz;

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
    void sendSms(String phoneNumber, SmsBiz smsBiz, List<String> params);
}
