package cn.huacloud.taxpreference.services.openapi;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiAccessKeyDO;

/**
 * @author wangkh
 */
public interface OpenApiService {

    ApiAccessKeyDO queryApiAccessKey(String accessKeyId, String accessKeySecret);

}
