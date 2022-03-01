package cn.huacloud.taxpreference.services.wwx;

import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallBackQueryDTO;

/**
 * 企业微信第三方应用支持服务
 * @author wangkh
 */
public interface WWXService {

    String callBackGet(CallBackQueryDTO query) throws Exception;

    String callBackPost(CallBackQueryDTO query, String bodyStr) throws Exception;
}
