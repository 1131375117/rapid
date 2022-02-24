package cn.huacloud.taxpreference.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangkh
 */
@Configuration
@ConfigurationProperties(prefix = "tax-preference.encrypt")
public class EncryptConfig {
    /**
     * AES 秘钥，16进制长度为16的字符串
     */
    private String key = "e17f1d1b629935ed";

    /**
     * 公共静态AES key，方便在加密类中获取
     */
    public static String AES_KEY;

    public EncryptConfig() {
        AES_KEY = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        AES_KEY = key;
    }
}
