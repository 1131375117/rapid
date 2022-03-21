package cn.huacloud.taxpreference.services.wework.client.entity;

import lombok.Data;

/**
 * 第三方应用凭证
 * @author wangkh
 */
@Data
public class SuiteToken {
    private Integer errcode;
    private String errmsg;
    private String suite_access_token;
    private Integer expires_in;

    @Data
    public static class Request {
        private String suite_id;
        private String suite_secret;
        private String suite_ticket;
    }
}
