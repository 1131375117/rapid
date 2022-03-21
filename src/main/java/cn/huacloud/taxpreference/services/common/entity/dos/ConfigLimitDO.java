package cn.huacloud.taxpreference.services.common.entity.dos;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fuhua
 */
@Accessors(chain = true)
@Data
public class ConfigLimitDO {


    /**
     * 限流策略，1：窗口策略，2：令牌桶策略, 数据库字段：limit_type
     */
    private byte limitType ;

    /**
     * 每秒可处理请求数, 数据库字段：exec_count_per_second
     */
    private Integer execCountPerSecond ;

    /**
     * 限流过期时间，默认1秒，即每durationSeconds秒允许多少请求（当limit_type=1时有效）, 数据库字段：durationSeconds
     */
    private Integer durationSeconds ;

    /**
     * 令牌桶容量, 数据库字段：token_bucket_count
     */
    private Integer tokenBucketCount;

    /**
     * 限流开启状态，1:开启，0关闭, 数据库字段：limit_status
     */
    private Byte limitStatus;

    /**
     * 窗口计数器
     */
    private volatile LoadingCache<Long, AtomicLong> counter;

    /**
     * 获取持续时间，1秒内限制请求，则duration设置2
     *
     * @return 返回缓存保存的值。
     */
    public int fetchDuration() {
        Integer durationSeconds = this.durationSeconds;
        if (durationSeconds == null || durationSeconds < 1) {
            durationSeconds = 1;
        }
        // 1秒内限制请求，则duration设置2
        return durationSeconds + 1;
    }

    public LoadingCache<Long, AtomicLong> getCounter() {
        if (counter == null) {
            synchronized (this) {
                if (counter == null) {
                    int duration = fetchDuration();
                    counter = CacheBuilder.newBuilder()
                            .expireAfterWrite(duration, TimeUnit.SECONDS)
                            .build(new CacheLoader<Long, AtomicLong>() {
                                @Override
                                public AtomicLong load(Long seconds) throws Exception {
                                    return new AtomicLong(0);
                                }
                            });
                }
            }
        }
        return counter;
    }

    /**
     * 令牌桶
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private volatile RateLimiter rateLimiter;

    public synchronized void initRateLimiter() {
        rateLimiter = RateLimiter.create(tokenBucketCount);
    }

    /**
     * 获取令牌桶
     *
     * @return
     */
    public RateLimiter fetchRateLimiter() {
        if (rateLimiter == null) {
            synchronized (this) {
                if (rateLimiter == null) {
                    rateLimiter = RateLimiter.create(tokenBucketCount);
                }
            }
        }
        return rateLimiter;
    }

}
