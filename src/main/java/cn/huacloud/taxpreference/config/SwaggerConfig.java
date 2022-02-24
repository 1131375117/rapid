package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.controllers.common.SysCodeController;
import cn.huacloud.taxpreference.controllers.consumer.PoliciesSearchController;
import cn.huacloud.taxpreference.controllers.producer.PoliciesController;
import cn.huacloud.taxpreference.controllers.sso.ConsumerSSOController;
import cn.huacloud.taxpreference.controllers.tool.SysToolController;
import cn.huacloud.taxpreference.controllers.user.ProducerUserController;
import cn.huacloud.taxpreference.openapi.apis.AuthApi;
import com.github.xiaoymin.knife4j.spring.plugin.ApiListingOrderReader;
import com.github.xiaoymin.knife4j.spring.plugin.OperationOrderBuilderPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wangkh
 */
@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean("commonDocket")
    public Docket commonDocket() {
        return docket("公共服务接口", SysCodeController.class);
    }

    @Bean("consumerDocket")
    public Docket consumerDocket() {
        return docket("前台接口", PoliciesSearchController.class);
    }

    @Bean("producerDocket")
    public Docket producerDocket() {
        return docket("后台接口", PoliciesController.class);
    }

    @Bean("ssoDocket")
    public Docket ssoDocket() {
        return docket("用户登录", ConsumerSSOController.class);
    }

    @Bean("userDocket")
    public Docket userDocket() {
        return docket("用户管理", ProducerUserController.class);
    }

    @Bean("toolDocket")
    public Docket toolDocket() {
        return docket("系统工具", SysToolController.class);
    }

    @Bean("openApiDocket")
    public Docket Docket() {
        return docket("OpenAPI开放接口", AuthApi.class)
                .securitySchemes(securitySchemes());
    }

    @Bean
    public ApiListingOrderReader apiListingOrderReader() {
        return new ApiListingOrderReader();
    }

    @Bean
    public OperationOrderBuilderPlugin operationOrderBuilderPlugin() {
        return new OperationOrderBuilderPlugin();
    }

    private List<SecurityScheme> securitySchemes() {
        return Collections.singletonList(new ApiKey("Token", "Token", "header"));
    }

    private Docket docket(String groupName, Class<?> clazz) {
        String basePackage = clazz.getPackage().getName();
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName(groupName)
                .globalResponses(HttpMethod.GET, Collections.singletonList(response("200", "请求成功")))
                .globalResponses(HttpMethod.POST, Collections.singletonList(response("200", "请求成功")))
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build();
    }

    private Response response(String code, String description) {
        return new Response(code,
                description,
                true,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
                );
    }
}
