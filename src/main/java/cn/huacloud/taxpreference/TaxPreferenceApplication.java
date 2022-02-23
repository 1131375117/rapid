package cn.huacloud.taxpreference;

import com.spring4all.swagger.EnableSwagger2Doc;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * Spring Boot 启动类
 * @author wangkh
 */
@Slf4j
@EnableSwagger2Doc
@MapperScan("cn.huacloud.taxpreference.services.*.mapper")
@SpringBootApplication
public class TaxPreferenceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TaxPreferenceApplication.class, args);
        // 打印 swagger 地址
        Environment environment = context.getBean(Environment.class);

        log.info("SwaggerUrl: http://localhost:{}/swagger-ui.html", environment.getProperty("server.port"));
    }
}
