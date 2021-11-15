package cn.huacloud.taxpreference.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tax-preference.sys")
public class SysConfig {

    /**
     * 系统管理密码
     */
    private String password = "12345678Aa";

}
