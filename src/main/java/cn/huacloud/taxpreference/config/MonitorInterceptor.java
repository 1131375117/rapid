package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.common.annotations.MonitorInterface;
import cn.huacloud.taxpreference.common.enums.user.RequestType;
import cn.huacloud.taxpreference.common.utils.MonitorUtil;
import cn.huacloud.taxpreference.services.common.MonitorService;
import cn.huacloud.taxpreference.services.common.entity.dos.ApiUserStatisticsDO;
import cn.huacloud.taxpreference.services.common.entity.dos.UserMonitorInfoDO;
import cn.huacloud.taxpreference.services.common.mapper.UserMonitorInfoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author fuhua
 **/
@Configuration
@RequiredArgsConstructor
public class MonitorInterceptor implements HandlerInterceptor {

    private final UserMonitorInfoMapper monitorInfoMapper;
    private final MonitorService monitorService;
  //  private final MonitorApiEventTrigger monitorApiEventTrigger;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(MonitorInterface.class)) {
            return true;
        }
        //设置请求开始时间
        String params = MonitorUtil.getRequestPayload(request);
        //获取方法
        request.setAttribute("startTime", LocalDateTime.now());
        request.setAttribute("params", params);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(MonitorInterface.class)) {
            return;
        }
        //获取url
        String path = request.getRequestURI();
        //获取接口请求方式
        String pattern = request.getMethod();
        //获取请求参数
        String parameterMap = objectMapper.writeValueAsString(request.getParameterMap());
        if (!RequestType.GET.name.equalsIgnoreCase(pattern)) {
            parameterMap = (String) request.getAttribute("params");
        }
        //获取token
        String token = request.getHeader("Token");
        String akId = MonitorUtil.accessKeyId.getIfPresent(token);
        //获取开始时间
        LocalDateTime startTime = (LocalDateTime) request.getAttribute("startTime");
        //获取调用时长
        Long invokeTime = System.currentTimeMillis() - startTime.toEpochSecond(ZoneOffset.ofHours(8));
        //获取结束时间
        LocalDateTime endTime = LocalDateTime.now();
        //获取响应状态
        String status = String.valueOf(response.getStatus());

        UserMonitorInfoDO userMonitorInfoDO = new UserMonitorInfoDO();
        userMonitorInfoDO
                .setPath(path)
                .setAkId(akId)
                .setRequestMethod(pattern)
                .setRequestParams(parameterMap)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setInvokeStatus(status)
                .setInvokeTime(invokeTime);
        if (ex != null) {
            userMonitorInfoDO.setInvokeMsg(ex.getMessage());
        } else {
            userMonitorInfoDO.setInvokeMsg("");
        }
        //写入
        monitorInfoMapper.insert(userMonitorInfoDO);
        //写入t_api_user_statistics

        ApiUserStatisticsDO apiUserStatisticsDO = new ApiUserStatisticsDO();

        apiUserStatisticsDO
                .setPath(path)
                .setRequestMethod(pattern)
                .setAkId(akId)
                .setMaxTime(invokeTime)
                .setMinTime(invokeTime)
                .setTotalTime(invokeTime)
                .setTotalRequestCount(1L)
        ;
        //查询是否存在
        ApiUserStatisticsDO userStatisticsDO = monitorService.queryOne(apiUserStatisticsDO);
        if (userStatisticsDO == null) {

            insertStatisticsDO(response, apiUserStatisticsDO);
        } else {
            updateStatisticsDO(response, invokeTime, userStatisticsDO);
        }


    }

    /*
     * 插入OpenAPI统计信息表
     */
    private void insertStatisticsDO(HttpServletResponse response, ApiUserStatisticsDO apiUserStatisticsDO) {
        if (response.getStatus() == 200) {
            apiUserStatisticsDO.setSuccessCount(1L);
            apiUserStatisticsDO.setErrorCount(0L);
        } else {
            apiUserStatisticsDO.setErrorCount(1L);
            apiUserStatisticsDO.setErrorCount(0L);
        }
        monitorService.insert(apiUserStatisticsDO);
       // monitorApiEventTrigger.saveEvent(apiUserStatisticsDO);
    }

    /*
     * 更新OpenAPI统计信息表
     */
    private void updateStatisticsDO(HttpServletResponse response, Long invokeTime, ApiUserStatisticsDO userStatisticsDO) {
        //最大调用时长
        Long maxTime = MonitorUtil.storeMaxTime(userStatisticsDO.getMaxTime(), invokeTime);
        userStatisticsDO.setMaxTime(maxTime);
        //最短调用时长
        Long minTime = MonitorUtil.storeMaxTime(userStatisticsDO.getMinTime(), invokeTime);
        userStatisticsDO.setMinTime(minTime);
        //总时长
        Long totalTime = MonitorUtil.totalTime(userStatisticsDO.getTotalTime(), invokeTime);
        userStatisticsDO.setTotalTime(totalTime);
        //获取总次数
        Long count = MonitorUtil.totalCount(userStatisticsDO.getTotalRequestCount());
        userStatisticsDO.setTotalRequestCount(count);
        //设置成功失败次数
        getStatusCount(response, userStatisticsDO);
        //写入mysql
        monitorService.update(userStatisticsDO);
        //monitorApiEventTrigger.updateEvent(userStatisticsDO);
    }

    private void getStatusCount(HttpServletResponse response, ApiUserStatisticsDO userStatisticsDO) {
        if (response.getStatus() == 200) {
            userStatisticsDO.setSuccessCount(userStatisticsDO.getSuccessCount() + 1);
        } else {
            userStatisticsDO.setErrorCount(userStatisticsDO.getErrorCount() + 1);
        }
    }


}
