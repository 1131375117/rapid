package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import cn.huacloud.taxpreference.services.wework.support.WeWorkConstants;
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
public class WeWorkConfig {

    private AppConfig taxPreference;

    private AppConfig taxTool;

    public AppConfig getAppConfigByAppName(String appName) {
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
}