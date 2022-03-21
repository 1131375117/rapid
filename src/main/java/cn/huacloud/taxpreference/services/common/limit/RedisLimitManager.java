package cn.huacloud.taxpreference.services.common.limit;


import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.RedisKeyUtil;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiStpUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 基于redis限流管理
 *
 * @author fuhua
 */
@Component
public class RedisLimitManager extends DefaultLimitManager {

    /**
     * 限流脚本
     */
    private static final String DEFAULT_LIMIT_LUA_FILE_PATH = "/limit.lua";

    private static final String TOKEN_LIMIT_LUA_FILE_PATH = "/request_rate_token_limiter.lua";

    private static final Long REDIS_SUCCESS = 1L;

    private StringRedisTemplate redisTemplate;
    private String limitScript;
    private String limitScriptSha1;
    private RedisScript redisRequestRateLimiterScript;

    public RedisLimitManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        Assert.notNull(redisTemplate, "redisTemplate不能为null");
        this.redisTemplate = new StringRedisTemplate(redisTemplate.getConnectionFactory());
        ClassPathResource limitLua = new ClassPathResource(getLimitLuaFilePath());
        try {
            //窗口策略lua
            this.limitScript = IOUtils.toString(limitLua.getInputStream(), StandardCharsets.UTF_8);
            this.limitScriptSha1 = DigestUtils.sha1Hex(this.limitScript);

            //令牌桶限流lua
            DefaultRedisScript redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(
                    new ClassPathResource(TOKEN_LIMIT_LUA_FILE_PATH)));
            this.redisRequestRateLimiterScript = redisScript;
            redisScript.setResultType(List.class);
        } catch (Exception e) {
            throw new RuntimeException("读取脚本文件失败，脚本路径:" + getLimitLuaFilePath(), e);
        }
    }

    public String getLimitLuaFilePath() {
        return DEFAULT_LIMIT_LUA_FILE_PATH;
    }


    @Override
    public double acquireToken(ConfigLimitDTO routeConfig) {
        String limitUserRedisKey = RedisKeyUtil.getLimitUserRedisKey(OpenApiStpUtil.getLoginIdAsString(), routeConfig);
        List<String> keys = getKeys(limitUserRedisKey);
        String[] args = new String[]{routeConfig.getReplenishRate() + "", routeConfig.getBurstCapacity() + "", Instant.now().getEpochSecond() + "", routeConfig.getRequestedTokens() + ""};
        List<Long> results = (List<Long>) this.redisTemplate.execute(redisRequestRateLimiterScript, keys, args);
        assert results != null;
        boolean allowed = results.get(0) == 1L;
        if (!allowed) {
            throw BizCode._4705.exception();
        }
        Long tokensLeft = results.get(1);
        System.out.println("tokens left ->{}："+tokensLeft);
        return results.get(0);
    }

    static List<String> getKeys(String key) {
        // use `{}` around keys to use Redis Key hash tags
        // this allows for using redis cluster

        // Make a unique key per user.
        String prefix = "request_rate_limiter.{" + key;

        // You need two Redis keys for Token Bucket.
        String tokenKey = prefix + "}.tokens";
        String timestampKey = prefix + "}.timestamp";
        return Arrays.asList(tokenKey, timestampKey);
    }

    @Override
    public boolean acquire(ConfigLimitDTO routeConfig) {

        String limitUserRedisKey = RedisKeyUtil.getLimitUserRedisKey(OpenApiStpUtil.getLoginIdAsString(), routeConfig);

        int limitCount = routeConfig.getExecCountPerSecond();

        int duration = routeConfig.getDurationSeconds();

        Object result = redisTemplate.execute(
                new RedisScript<Long>() {
                    @Override
                    public String getSha1() {
                        return limitScriptSha1;
                    }

                    @Override
                    public Class<Long> getResultType() {
                        return Long.class;
                    }

                    @Override
                    public String getScriptAsString() {
                        return limitScript;
                    }
                },
                // KEYS[1] key
                Collections.singletonList(limitUserRedisKey),
                // ARGV[1] limit
                String.valueOf(limitCount),
                // ARGV[2] expire
                String.valueOf(duration)
        );
        return REDIS_SUCCESS.equals(result);
    }

}
