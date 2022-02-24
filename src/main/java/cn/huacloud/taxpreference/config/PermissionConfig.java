package cn.huacloud.taxpreference.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tax-preference.permission")
public class PermissionConfig {
    /**
     * 扫描权限并更新保存，开启后每次启动都会去更新数据库
     */
    private Boolean scanPermissionAndSave = true;
}
