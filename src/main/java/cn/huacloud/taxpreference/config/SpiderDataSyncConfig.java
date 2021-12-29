package cn.huacloud.taxpreference.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 爬虫数据同步配置
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spider-sync")
public class SpiderDataSyncConfig {

    /**
     * 爬虫数据同步
     */
    private Boolean enabled = false;
    /**
     * 调度cron表达式，默认每天凌晨2点执行
     */
    private String cron = "0 0 2 * * ?";
    /**
     * 爬虫库数据源
     */
    private DataSource dataSource;

    @Data
    public static class DataSource {
        /**
         * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
         */
        private String driverClassName;
        /**
         * JDBC URL of the database.
         */
        private String url;
        /**
         * Login username of the database.
         */
        private String username;
        /**
         * Login password of the database.
         */
        private String password;
    }
}
