package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.services.common.MonitorService;
import cn.huacloud.taxpreference.sync.es.trigger.impl.MonitorApiEventTrigger;
import cn.huacloud.taxpreference.sync.es.trigger.impl.MonitorUserApiInfoEventTrigger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fuhua
 **/
@Configuration
@RequiredArgsConstructor
public class MonitorConfig implements WebMvcConfigurer {

    private final MonitorService monitorService;
    private final MonitorApiEventTrigger monitorApiEventTrigger;
    private final MonitorUserApiInfoEventTrigger monitorUserApiInfoEventTrigger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new MonitorInterceptor(monitorService, monitorApiEventTrigger, monitorUserApiInfoEventTrigger)).addPathPatterns("/**");
    }
}