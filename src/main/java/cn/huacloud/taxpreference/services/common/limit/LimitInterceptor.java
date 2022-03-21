package cn.huacloud.taxpreference.services.common.limit;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.huacloud.taxpreference.common.annotations.LimitApi;
import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.LimitType;
import cn.huacloud.taxpreference.common.utils.IpUtil;
import cn.huacloud.taxpreference.services.common.SysParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;

/**
 * @author fuhua
 **/
@Configuration
@RequiredArgsConstructor
public class LimitInterceptor implements HandlerInterceptor {

    private final RedisLimitManager redisLimitManager;

    private final DefaultLimitManager defaultLimitManager;

    private final SysParamService sysParamService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request = SpringMVCUtil.getRequest();
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(LimitApi.class)) {
            return true;
        }

        ConfigLimitDTO configLimitDto = sysParamService.getObjectParamByTypes(Collections.singletonList(SysParamTypes.LIMIT_BASE), ConfigLimitDTO.class);
        configLimitDto.setRequestMethod(request.getMethod());
        configLimitDto.setPath(request.getRequestURI());
        configLimitDto.setIp(IpUtil.getIp(request));

        //获取限流类型
        int limitType = configLimitDto.getLimitType();

        // 如果是窗口策略
        if (limitType == LimitType.LEAKY_BUCKET.getType()) {
            boolean acquire = redisLimitManager.acquire(configLimitDto);
            // 被限流，返回错误信息
            if (!acquire) {
                throw BizCode._4705.exception();
            }
        } else if (limitType == LimitType.TOKEN_BUCKET.getType()) {
            //令牌桶限流算法
           redisLimitManager.acquireToken(configLimitDto);
        }
        return true;
    }

}
