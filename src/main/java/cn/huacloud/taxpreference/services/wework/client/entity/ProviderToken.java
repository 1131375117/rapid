package cn.huacloud.taxpreference.services.wework.client.entity;

import lombok.Data;

/**
 * 服务商凭证
 * @author wangkh
 */
@Data
public class ProviderToken {
    private String provider_access_token;
    private Integer expires_in;

    @Data
    public static class Request {
        private String corpid;
        private String provider_secret;
    }
}
