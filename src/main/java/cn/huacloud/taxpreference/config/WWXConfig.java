package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.services.wwx.ase.WXBizMsgCrypt;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 企业微信配置类
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wwx")
public class WWXConfig {

    private String corpId;
    private String providerSecret;
    private String suiteId;
    private String secret;
    private String token;
    private String encodingAesKey;

    @Bean
    public WXBizMsgCrypt wxBizMsgCrypt() throws Exception {
        return new WXBizMsgCrypt(token, encodingAesKey, corpId);
    }
}
