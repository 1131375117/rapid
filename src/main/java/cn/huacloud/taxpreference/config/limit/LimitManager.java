package cn.huacloud.taxpreference.config.limit;

/**
 * 限流管理
 * @author fuhua
 */
public interface LimitManager {

    /**
     * 从令牌桶中获取令牌，如果使用{@link cn.huacloud.taxpreference.common.enums.LimitType#TOKEN_BUCKET
     * RateType.TOKEN_BUCKET}限流策略，则该方法生效
     * 
     * @param configLimitDto 路由配置
     * @return 返回耗时时间，秒
     */
    double acquireToken(ConfigLimitDto configLimitDto);

    /**
     * 是否需要限流，如果使用{@link cn.huacloud.taxpreference.common.enums.LimitType#LEAKY_BUCKET
     * RateType.LIMIT}限流策略，则该方法生效
     * 
     * @param configLimitDto 路由配置
     * @return 如果返回true，表示可以执行业务代码，返回false则需要限流
     */
    boolean acquire(ConfigLimitDto configLimitDto);

}
