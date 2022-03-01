package cn.huacloud.taxpreference.services.wwx.entity.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 回调查询参数
 * 注意不要修改字段名称，否则会导致参数无法完成绑定
 * @author wangkh
 */
@Data
public class CallbackQueryDTO {
    /**
     * 	企业微信加密签名
     */
    private String msg_signature;
    /**
     * 时间戳。与nonce结合使用，用于防止请求重放攻击。
     */
    private String timestamp;
    /**
     * 随机数。与timestamp结合使用，用于防止请求重放攻击。
     */
    private String nonce;
    /**
     * 	加密的字符串。需要解密得到消息内容明文，解密后有random、msg_len、msg、receiveid四个字段，其中msg即为消息内容明文
     */
    private String echostr;

    public String getMsgSignature() {
        return msg_signature;
    }

    public String getEchoStr() {
        return echostr;
    }
}
