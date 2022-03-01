package cn.huacloud.taxpreference.services.wwx;

import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackQueryDTO;

/**
 * 企业微信第三方应用支持服务
 * @author wangkh
 */
public interface WWXService {

    String verifyURL(CallbackQueryDTO queryDTO) throws Exception;

    String installCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception;

    String dataCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception;

    String instructCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception;
}
