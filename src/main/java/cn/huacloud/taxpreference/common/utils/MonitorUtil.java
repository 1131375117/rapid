package cn.huacloud.taxpreference.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * @author fuhua
 **/
public class MonitorUtil {

    /**
     * 缓存
     */
    public static final Cache<Object, String> accessKeyId = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.DAYS)
            .build();

    /**
     * 获取request请求信息
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String getRequestPayload(HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        return objectMapper.writeValueAsString(responseStrBuilder.toString());
    }

    /**
     * 比较时长max
     */
    public static synchronized Long storeMaxTime(Long maxTime, Long thisTime) {
        if (thisTime > maxTime) {
            maxTime = thisTime;
        }
        return maxTime;
    }

    /**
     * 比较总时长
     */
    public static synchronized Long totalTime(Long totalTime, Long thisTime) {

        return totalTime + thisTime;
    }

    /**
     * 比较总次数
     */
    public static synchronized Long totalCount(Long totalCount) {

        return totalCount + 1;
    }


    /**
     * 比较时长short
     */
    public static synchronized Long storeMinTime(Long minTime, Long thisTime) {
        if (thisTime < minTime) {
            minTime = thisTime;
        }
        return minTime;
    }
}
