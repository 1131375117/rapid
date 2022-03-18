package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.huacloud.taxpreference.common.utils.ConsumerStpUtil;
import cn.huacloud.taxpreference.openapi.auth.OpenApiStpUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * mvc配置类
 * @author wangkh
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer, CommandLineRunner {

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器，并排除不需要注解鉴权的接口地址
        registry.addInterceptor(new SaAnnotationInterceptor()).addPathPatterns("/**");
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
