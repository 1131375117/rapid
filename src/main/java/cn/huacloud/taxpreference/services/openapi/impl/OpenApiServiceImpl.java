package cn.huacloud.taxpreference.services.openapi.impl;

import cn.huacloud.taxpreference.services.openapi.OpenApiService;
import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiAccessKeyDO;
import cn.huacloud.taxpreference.services.openapi.mapper.ApiAccessKeyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class OpenApiServiceImpl implements OpenApiService {

    private final ApiAccessKeyMapper apiAccessKeyMapper;

    @Override
    public ApiAccessKeyDO queryApiAccessKey(String accessKeyId, String accessKeySecret) {
        return apiAccessKeyMapper.queryByAccessKeyIdAndAccessKeySecret(accessKeyId, accessKeySecret);
    }
}
