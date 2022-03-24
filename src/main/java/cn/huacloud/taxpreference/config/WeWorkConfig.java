package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import cn.huacloud.taxpreference.services.wework.support.WeWorkConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 企业微信配置类
 * 这个项目集成了两个应用的配置，所以配置形式是多应用的。
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "wwx")
public class WeWorkConfig {
    /**
     * 税小秘应用
     */
    private AppConfig taxPreference;

    /**
     * 税务小工具应用
     */
    private AppConfig taxTool;

    /**
     * http代理配置
     */
    private ProxyConfig proxy;


    public AppConfig getAppConfigByAppName(String appName) {
        // TODO 新加入的应用需要在这里配置
        if (WeWorkConstants.APP_NAME_TAX_PREFERENCE.equals(appName)) {
            return taxPreference;
        } else if (WeWorkConstants.APP_NAME_TAX_TOOL.equals(appName)) {
            return taxTool;
        }
        throw new RuntimeException("找不到对应的AppConfig，appName：" + appName);
    }

    public AppConfig getAppConfigByUri(String uri) {
        // TODO 新加入的应用需要在这里配置
        return taxPreference;
    }

    public String getAppNameByUri(String requestURI) {
        // TODO 新加入的应用需要在这里配置
        return WeWorkConstants.APP_NAME_TAX_PREFERENCE;
    }

    @Data
    public static class ProxyConfig {
        private Boolean enable = false;
        private String host;
        private Integer port;
        private String username;
        private String password;

        public boolean useAuth() {
            return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password);
        }
    }
}
