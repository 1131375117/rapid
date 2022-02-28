package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.services.common.MonitorService;
import cn.huacloud.taxpreference.services.common.mapper.UserMonitorInfoMapper;

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
    private final UserMonitorInfoMapper userMonitorInfoMapper;
    private final MonitorService monitorService;
  //  private final MonitorApiEventTrigger monitorApiEventTrigger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new MonitorInterceptor(userMonitorInfoMapper, monitorService)).addPathPatterns("/**");
    }
}
