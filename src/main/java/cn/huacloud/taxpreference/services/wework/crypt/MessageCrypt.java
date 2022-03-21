package cn.huacloud.taxpreference.services.wework.crypt;

import cn.huacloud.taxpreference.services.wework.entity.model.CallbackQuery;

/**
 * @author wangkh
 */
public interface MessageCrypt {

    String verifyUrl(CallbackQuery query) throws Exception;

    String decryptMsg(CallbackQuery query, String postData) throws Exception;
}
