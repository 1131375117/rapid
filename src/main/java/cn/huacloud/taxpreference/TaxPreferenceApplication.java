package cn.huacloud.taxpreference;

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
@MapperScan("cn.huacloud.taxpreference.services.*.mapper")
@SpringBootApplication
public class TaxPreferenceApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(TaxPreferenceApplication.class, args);
        // 打印 swagger 地址
        Environment environment = context.getBean(Environment.class);

        log.info("SwaggerUrl: http://localhost:{}/doc.html", environment.getProperty("server.port"));
    }
}
