package cn.huacloud.taxpreference.config.limit;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

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

    private static final Long REDIS_SUCCESS = 1L;

    private StringRedisTemplate redisTemplate;
    private String limitScript;
    private String limitScriptSha1;

    public RedisLimitManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        Assert.notNull(redisTemplate, "redisTemplate不能为null");
        this.redisTemplate = new StringRedisTemplate(redisTemplate.getConnectionFactory());
        ClassPathResource limitLua = new ClassPathResource(getLimitLuaFilePath());
        try {
            this.limitScript = IOUtils.toString(limitLua.getInputStream(), StandardCharsets.UTF_8);
            this.limitScriptSha1 = DigestUtils.sha1Hex(this.limitScript);
        } catch (Exception e) {
            throw new RuntimeException("读取脚本文件失败，脚本路径:" + getLimitLuaFilePath(), e);
        }
    }

    public String getLimitLuaFilePath() {
        return DEFAULT_LIMIT_LUA_FILE_PATH;
    }

    @Override
    public boolean acquire(ConfigLimitDto routeConfig) {
        String key = "api:lmt:"+routeConfig.getRequestMethod()+":"+routeConfig.getPath();
        int limitCount = routeConfig.getExecCountPerSecond();
        int duration = routeConfig.fetchDuration();
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
                Collections.singletonList(key),
                // ARGV[1] limit
                String.valueOf(limitCount),
                // ARGV[2] expire
                String.valueOf(duration)
        );
        return REDIS_SUCCESS.equals(result);
    }

}
