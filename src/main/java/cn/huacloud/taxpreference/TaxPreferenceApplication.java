package cn.huacloud.taxpreference;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Spring Boot 启动类
 * @author wangkh
 */
@EnableSwagger2
@MapperScan("cn.huacloud.taxpreference.services.*.mapper")
@SpringBootApplication
public class TaxPreferenceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaxPreferenceApplication.class, args);
        // 打印 swagger 地址
        /*Environment environment = context.getBean(Environment.class);
        log.info("SwaggerUrl: http://localhost:{}/swagger-ui.html", environment.getProperty("server.port"));*/
    }
}
