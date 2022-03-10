package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.interceptor.SaAnnotationInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.strategy.SaStrategy;
import cn.huacloud.taxpreference.common.utils.ConsumerStpUtil;
import cn.huacloud.taxpreference.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.wework.auth2.WeWorkLoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token配置类
 * @author wangkh
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer, CommandLineRunner {

    @Autowired
    private WeWorkLoginInterceptor weWorkLoginInterceptor;

    /**
     * 注册Sa-Token的注解拦截器，打开注解式鉴权功能
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器，并排除不需要注解鉴权的接口地址 (与登录拦截器无关)
        registry.addInterceptor(weWorkLoginInterceptor).addPathPatterns("/**");
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
