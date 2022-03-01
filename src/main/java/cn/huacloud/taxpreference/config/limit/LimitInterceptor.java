package cn.huacloud.taxpreference.config.limit;

import cn.huacloud.taxpreference.common.annotations.LimitApi;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.LimitType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author fuhua
 **/
@Configuration
@RequiredArgsConstructor
public class LimitInterceptor implements HandlerInterceptor {

    private final RedisLimitManager redisLimitManager;
    private final DefaultLimitManager defaultLimitManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(LimitApi.class)) {
            return true;
        }
        ConfigLimitDto configLimitDto = new ConfigLimitDto();
        configLimitDto.setRequestMethod(request.getMethod());
        configLimitDto.setPath(request.getRequestURI());
        //获取限流类型
        int limitType = method.getAnnotation(LimitApi.class).limitType().getType();
        // 如果是窗口策略
        if (limitType == LimitType.LEAKY_BUCKET.getType()) {
            boolean acquire = redisLimitManager.acquire(configLimitDto);
            // 被限流，返回错误信息
            if (!acquire) {
                throw BizCode._4705.exception();
            }
        } else if (limitType == LimitType.TOKEN_BUCKET.getType()) {
            // defaultLimitManager.acquire(configLimitDto);
            throw BizCode._500.exception("敬请期待!");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }


}
