package cn.huacloud.taxpreference.services.wework.impl;

import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.config.WeWorkConfig;
import cn.huacloud.taxpreference.services.wework.WeWorkTokenService;
import cn.huacloud.taxpreference.services.wework.client.WeWorkServiceClient;
import cn.huacloud.taxpreference.services.wework.client.entity.SuiteToken;
import cn.huacloud.taxpreference.services.wework.client.entity.UserInfo3rd;
import cn.huacloud.taxpreference.services.wework.entity.dtos.UserInfo3rdDTO;
import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import cn.huacloud.taxpreference.services.wework.support.ObjectMapperProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WeWorkTokenServiceImpl implements WeWorkTokenService {

    public static final String PREFIX = "wwx";
    public static final String SUITE_TICKET = "suite_ticket";
    public static final String SUITE_TOKEN = "suite_token";

    private final RedisTemplate<String, String> redisTemplate;

    private final WeWorkServiceClient weWorkServiceClient;

    private final WeWorkConfig weWorkConfig;

    public static String getKey(String appName, String tokenName) {
        return PREFIX + ":" + appName + ":" + tokenName;
    }

    @Override
    public void setSuiteTicket(String appName, String suiteTicket) {
        String key = getKey(appName, SUITE_TICKET);
        redisTemplate.opsForValue().set(key, suiteTicket);
    }

    @Override
    public String getSuiteTicket(String appName) {
        String key = getKey(appName, SUITE_TICKET);
        String suiteTicket = redisTemplate.opsForValue().get(key);
        log.error("通过key: {} 没有获取到对应值", key);
        return suiteTicket;
    }

    @Override
    public String getSuiteToken(String appName) {
        String key = getKey(appName, SUITE_TOKEN);
        String suiteToken = redisTemplate.opsForValue().get(key);
        if (suiteToken != null) {
            return suiteToken;
        }
        SuiteToken.Request request = new SuiteToken.Request();
        AppConfig appConfig = weWorkConfig.getAppConfigByAppName(appName);
        request.setSuite_id(appConfig.getSuiteId());
        request.setSuite_secret(appConfig.getSecret());
        request.setSuite_ticket(getSuiteTicket(appName));
        SuiteToken response = weWorkServiceClient.getSuiteToken(request);
        if (response.getErrcode() != null && response.getErrcode() != 0) {
            throw  new TaxPreferenceException(response.getErrcode(), "获取suite_access_token异常\n"
                    + ObjectMapperProvider.writeJsonPrettyString(response));
        }
        String suite_access_token = response.getSuite_access_token();
        int timeout = response.getExpires_in() - 60;
        redisTemplate.opsForValue().set(key, suite_access_token);
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        return suite_access_token;
    }

    @Override
    public UserInfo3rdDTO getUserInfo3rdDTO(String appName, String code) {
        UserInfo3rd userInfo3rd = weWorkServiceClient.getUserInfo3rd(getSuiteToken(appName), code);
        log.info("UserInfo3rd：\n{}", ObjectMapperProvider.writeJsonPrettyString(userInfo3rd));
        if (userInfo3rd.getErrcode() != 0) {
            String json = ObjectMapperProvider.writeJsonPrettyString(this);
            throw new TaxPreferenceException(userInfo3rd.getErrcode(), "获取访问用户身份失败：\n" + json);
        }
        UserInfo3rdDTO dto = new UserInfo3rdDTO();
        dto.setUserId(userInfo3rd.getUserId());
        dto.setOpenUserId(userInfo3rd.getOpen_userid());
        dto.setOpenId(userInfo3rd.getOpenId());
        dto.setCorpId(userInfo3rd.getCorpId());
        return dto;
    }
}
