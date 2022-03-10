package cn.huacloud.taxpreference.services.wework.entity.model;

import lombok.Data;

/**
 * @author wangkh
 */
@Data
public class CallbackQuery {
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
}
