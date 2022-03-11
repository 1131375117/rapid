package cn.huacloud.taxpreference.services.wework.client.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/**
 * @author wangkh
 */

public class WeWorkFeignConfiguration {
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(10, TimeUnit.SECONDS, 30, TimeUnit.MINUTES, true);
    }
}
