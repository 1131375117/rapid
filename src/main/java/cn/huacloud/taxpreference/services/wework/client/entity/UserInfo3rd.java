package cn.huacloud.taxpreference.services.wework.client.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wangkh
 */
@Data
public class UserInfo3rd {
    private Integer errcode;
    private String errmsg;
    @JsonProperty("CorpId")
    private String CorpId;
    @JsonProperty("UserId")
    private String UserId;
    @JsonProperty("DeviceId")
    private String DeviceId;
    private List<Object> parents;
    private String open_userid;
    @JsonProperty("OpenId")
    private String OpenId;
}
