package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.common.annotations.MonitorInterface;
import cn.huacloud.taxpreference.services.common.entity.dos.ApiUserStatisticsDO;
import cn.huacloud.taxpreference.services.common.entity.dos.UserMonitorInfoDO;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

/**
 * @author fuhua
 **/
@Configuration
public class MonitorInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //获取方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(MonitorInterface.class)) {
            //设置请求开始时间
            request.setAttribute("startTime", System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(MonitorInterface.class)) {
            //获取url
            String path = request.getRequestURL().toString();
            //获取接口请求方式
            String pattern = request.getMethod();
            //获取请求参数
            Map<String, String[]> parameterMap = request.getParameterMap();
            //获取token
            String token = request.getHeader("Token");
            //获取开始时间
            LocalDateTime startTime = LocalDateTime.ofEpochSecond((Long) request.getAttribute("startTime"), 0, ZoneOffset.ofHours(8));
            //获取调用时长
            Long invokeTime = System.currentTimeMillis() - (Long) request.getAttribute("startTime");
            //获取结束时间
            LocalDateTime endTime = LocalDateTime.now();

            ApiUserStatisticsDO apiUserStatisticsDO = new ApiUserStatisticsDO();
            //
            apiUserStatisticsDO
                    .setPath(path)
                    .setRequestMethod(pattern);
            UserMonitorInfoDO userMonitorInfoDO = new UserMonitorInfoDO();
            userMonitorInfoDO
                    .setPath(path)
                    .setRequestMethod(pattern)
                    .setRequestParams(parameterMap.toString())
                    .setStartTime(startTime)
                    .setEndTime(endTime)
                    .setInvokeTime(invokeTime);

        }
    }
}
