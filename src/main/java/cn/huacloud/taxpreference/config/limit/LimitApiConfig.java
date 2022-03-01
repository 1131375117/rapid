package cn.huacloud.taxpreference.config.limit;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LimitInterceptor(redisLimitManager,defaultLimitManager)).addPathPatterns("/**");
    }
}