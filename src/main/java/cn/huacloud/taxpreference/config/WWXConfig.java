package cn.huacloud.taxpreference.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 企业微信配置类
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wwx")
public class WWXConfig {

    private App taxPreference;

    private App taxTool;

    @Data
    public static class App {
        /**
         * 每个服务商同时也是一个企业微信的企业，都有唯一的corpid
         */
        private String corpId;
        /**
         * 作为服务商身份的调用凭证，应妥善保管好该密钥，务必不能泄漏
         */
        private String providerSecret;
        /**
         * suiteid为应用的唯一身份标识
         */
        private String suiteId;
        /**
         * suite_secret为对应的调用身份密钥
         */
        private String secret;
        /**
         * 应用token，在管理页面随机生成
         */
        private String token;
        /**
         * 应用encodingAesKey，在管理页面随机生成
         */
        private String encodingAesKey;
    }
}
