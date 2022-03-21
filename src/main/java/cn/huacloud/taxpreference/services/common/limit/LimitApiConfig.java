package cn.huacloud.taxpreference.services.common.limit;

import cn.huacloud.taxpreference.services.common.SysParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fuhua
 **/
@Configuration
@RequiredArgsConstructor
public class LimitApiConfig implements WebMvcConfigurer {
    private final RedisLimitManager redisLimitManager;

    private final DefaultLimitManager defaultLimitManager;

    private final SysParamService sysParamService;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

         registry.addInterceptor(new LimitInterceptor(
                 redisLimitManager, defaultLimitManager, sysParamService))
                 .addPathPatterns("/**");
    }
}