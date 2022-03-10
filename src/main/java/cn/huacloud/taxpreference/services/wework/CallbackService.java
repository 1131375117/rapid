package cn.huacloud.taxpreference.services.wework;

import cn.huacloud.taxpreference.services.wework.entity.model.CallbackQuery;

/**
 * @author wangkh
 */
public interface CallbackService {
    String SUCCESS = "success";
    String FAIL = "fail";

    String verifyUrl(String appName, CallbackQuery query) throws Exception;
    String installCallback(String appName, CallbackQuery query, String postData) throws Exception;
    String dataCallback(String appName, CallbackQuery query, String postData) throws Exception;
    String instructCallback(String appName, CallbackQuery query, String postData) throws Exception;
}
