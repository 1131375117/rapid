package cn.huacloud.taxpreference.services.message.entity.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
@Getter
@Setter
public class TencentSmsParamDTO {
    // 密钥ID
    private String secretId;
    // 密钥Key
    private String secretKey;
    // 所属区域
    private String region;
    // 短信SdkAppId
    private String smsSdkAppId;
    // 短信签名内容
    private String signName;
    // 短信模板ID
    private String templateId;
}
