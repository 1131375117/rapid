package cn.huacloud.taxpreference.config.limit;


import cn.huacloud.taxpreference.common.enums.LimitType;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fuhua
 */
@Slf4j
@Component
public class DefaultLimitManager implements LimitManager {

    @Override
    public double acquireToken(ConfigLimitDto routeConfig) {
        if (routeConfig.getLimitStatus() == ConfigLimitDto.LIMIT_STATUS_CLOSE) {
            return 0;
        }
        if (LimitType.LEAKY_BUCKET.getType() == routeConfig.getLimitType()) {
            return 0;
        }
        return routeConfig.fetchRateLimiter().acquire();
    }


    @Override
    public boolean acquire(ConfigLimitDto routeConfig) {
        if (routeConfig.getLimitStatus() == ConfigLimitDto.LIMIT_STATUS_CLOSE) {
            return true;
        }
        if (LimitType.TOKEN_BUCKET.getType() == routeConfig.getLimitType()) {
            return true;
        }
        int execCountPerSecond = routeConfig.getExecCountPerSecond();
        try {
            LoadingCache<Long, AtomicLong> counter = routeConfig.getCounter();
            // 被限流了
            return counter.get(routeConfig.getId()).incrementAndGet() <= execCountPerSecond;
        } catch (ExecutionException e) {
            log.error("窗口限流出错，routeConfig:{}", routeConfig, e);
            return false;
        }
    }

}
