package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.huacloud.taxpreference.common.utils.ConsumerStpUtil;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.openapi.auth.OpenUserIdInterceptor;
import cn.huacloud.taxpreference.services.user.ChannelUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc配置类
 * @author wangkh
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer, CommandLineRunner {

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器，并排除不需要注解鉴权的接口地址
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
        // 注册
        registry.addInterceptor(new OpenUserIdInterceptor()).addPathPatterns("/open-api/**");
    }

    @Override
    public void run(String... args) throws Exception {
        // 重写Sa-Token的注解处理器，增加注解合并功能
        SaStrategy.me.getAnnotation = AnnotatedElementUtils::getMergedAnnotation;
        // 解决匿名内部类懒加载的问题
        StpUtil.getLoginType();
        ConsumerStpUtil.getLoginType();
        OpenApiStpUtil.getLoginType();
    }
}
