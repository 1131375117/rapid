package cn.huacloud.taxpreference.services.wework.client.entity;

import lombok.Data;

import java.util.List;

/**
 * @author wangkh
 */
@Data
public class PermanentCode {
    private String errcode;
    private String errmsg;
    private String access_token;
    private Integer expires_in;
    private String permanent_code;
    private dealer_corp_info dealer_corp_info;
    private auth_corp_info auth_corp_info;
    private auth_info auth_info;
    private register_code_info register_code_info;
    private String state;

    @Data
    public static class Request {
        private String auth_code;
    }

    @Data
    public static class dealer_corp_info {
        String corpid;
        String corp_name;
    }
    @Data
    public static  class auth_corp_info {
        String corpid;
        String corp_name;
        String corp_type;
        String corp_square_logo_url;
        String corp_user_max;
        String corp_full_name;
        String verified_end_time;
        String subject_type;
        String corp_wxqrcode;
        String corp_scale;
        String corp_industry;
        String corp_sub_industry;
    }
    @Data
    public static class auth_info {
        List<agent> agent;
    }
    @Data
    public static class agent {
        String agentid;
        String name;
        String round_logo_url;
        String square_logo_url;
        String appid;
        String auth_mode;
        String is_customized_app;
        privilege privilege;
        shared_from shared_from;
    }
    @Data
    public static class privilege {
        String level;
        List<Integer> allow_party;
        List<String> allow_user;
        List<Integer> allow_tag;
        List<Integer> extra_party;
        List<String> extra_user;
        List<Integer> extra_tag;
    }
    @Data
    public static class shared_from {
        String corpid;
        Integer share_type;
    }

    @Data
    public static class auth_user_info {
        String userid;
        String open_userid;
        String name;
        String avatar;
    }
    @Data
    public static class register_code_info {
        String register_code;
        String template_id;
        String state;
    }
}
