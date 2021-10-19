package cn.huacloud.taxpreference.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通用组件配置类
 * @author wangkh
 */
@Configuration
public class CommonConfig {

    /**
     * Object Mapper属性配置
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        // 反序列化忽略未定义的属性，宽泛前端入参
        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
