package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiStpUtil;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties("open-api")
public class OpenApiConfig implements InitializingBean {
    /**
     * OpenAPI的鉴权同样是使用SaToken实现的，这里的配置
     */
    private SaTokenConfig tokenConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 设置自定义配置
        OpenApiStpUtil.openApiTokenConfig = tokenConfig;
    }
}
