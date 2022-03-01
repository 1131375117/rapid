package cn.huacloud.taxpreference.services.wwx.entity.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author wangkh
 */
@Data
public class CallBackQueryDTO {
    @JsonProperty("msg_signature")
    private String msgSignature;
    private String timestamp;
    private String nonce;
    @JsonProperty("echostr")
    private String echoStr;
}
