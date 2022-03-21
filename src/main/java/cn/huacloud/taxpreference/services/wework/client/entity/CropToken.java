package cn.huacloud.taxpreference.services.wework.client.entity;

import lombok.Data;

/**
 * 企业凭证
 * @author wangkh
 */
@Data
public class CropToken {
    private Integer errcode;
    private String errmsg;
    private String access_token;
    private Integer expires_in;

    @Data
    public static class Request {
        private String auth_corpid;
        private String permanent_code;
    }
}
