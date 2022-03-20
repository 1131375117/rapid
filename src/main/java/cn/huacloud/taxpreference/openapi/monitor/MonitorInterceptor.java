package cn.huacloud.taxpreference.openapi.monitor;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.huacloud.taxpreference.services.openapi.monitor.MonitorApi;
import cn.huacloud.taxpreference.common.constants.TaxBaseConstants;
import cn.huacloud.taxpreference.common.utils.MonitorUtil;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.openapi.MonitorService;
import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiStatisticsDO;
import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiMonitorInfoDO;
import cn.huacloud.taxpreference.sync.es.trigger.impl.MonitorApiEventTrigger;
import cn.huacloud.taxpreference.sync.es.trigger.impl.MonitorUserApiInfoEventTrigger;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

/**
 * @author fuhua
 **/
@Deprecated
//@Configuration
@RequiredArgsConstructor
public class MonitorInterceptor implements HandlerInterceptor {

    private final MonitorService monitorService;
    private final MonitorApiEventTrigger monitorApiEventTrigger;
    private final MonitorUserApiInfoEventTrigger monitorUserApiInfoEventTrigger;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(MonitorApi.class)) {
            return true;
        }

        //RequestWrapper requestWrapper = new RequestWrapper(SpringMVCUtil.getRequest());
        //String params = MonitorUtil.getRequestPayload(requestWrapper);

        //设置请求开始时间
        String params = MonitorUtil.getRequestPayload(SpringMVCUtil.getRequest());
        SpringMVCUtil.getRequest().setAttribute(TaxBaseConstants.START_TIME, System.currentTimeMillis());
        SpringMVCUtil.getRequest().setAttribute(TaxBaseConstants.PARAMS, params);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        monitorApiInfo(request, response, (HandlerMethod) handler, ex);
    }

    private synchronized void monitorApiInfo(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws IOException {
        if (handler == null) {
            return;
        }
        HandlerMethod handlerMethod = handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(MonitorApi.class)) {
            return;
        }
        //获取token
        String token = SpringMVCUtil.getRequest().getHeader(TaxBaseConstants.TOKEN);
        if (StringUtils.isEmpty(token)) {
            return;
        }
        String akId = StringUtils.substringAfterLast(OpenApiStpUtil.getSession().getId(), ":");

        //获取url
        String path = SpringMVCUtil.getRequest().getRequestURI();

        //获取接口请求方式
        String pattern = SpringMVCUtil.getRequest().getMethod();

        //获取请求参数
        ObjectMapper objectMapper = new ObjectMapper();
        String parameterMap;

        Map<String, String[]> paramArr = SpringMVCUtil.getRequest().getParameterMap();

        if (!paramArr.isEmpty()) {
            parameterMap = objectMapper.writeValueAsString(SpringMVCUtil.getRequest().getParameterMap());
        } else {
            parameterMap = (String) request.getAttribute(TaxBaseConstants.PARAMS);

        }

/*        if("application/x-www-form-urlencoded".equals(request.getContentType())&&RequestType.POST.name.equalsIgnoreCase(pattern)){
            parameterMap = (String) request.getAttribute(TaxBaseConstants.PARAMS);
        }*/
/*        if (!RequestType.GET.name.equalsIgnoreCase(pattern)) {
            parameterMap = (String) request.getAttribute(TaxBaseConstants.PARAMS);
        }*/


        //获取开始时间
        Long startTime = (Long) SpringMVCUtil.getRequest().getAttribute(TaxBaseConstants.START_TIME);

        //获取调用时长
        Long invokeTime = System.currentTimeMillis() - startTime;

        //获取结束时间
        LocalDateTime endTime = LocalDateTime.now();

        //获取响应状态
        ResultVO<Void> resultVO = (ResultVO<Void>) SpringMVCUtil.getRequest().getAttribute(TaxBaseConstants.REQUEST_KEY);
        String status = resultVO == null ? "200" : String.valueOf(resultVO.getCode());

        ApiMonitorInfoDO apiMonitorInfoDO = new ApiMonitorInfoDO();
        apiMonitorInfoDO
                .setPath(path)
                .setAkId(akId)
                .setRequestMethod(pattern)
                .setRequestParams(parameterMap)
                .setStartTime(new Date(startTime).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime())
                .setEndTime(endTime)
                .setInvokeStatus(status)
                .setInvokeTime(invokeTime);
        apiMonitorInfoDO.setInvokeMsg(resultVO == null ? "" : resultVO.getMsg());
        //写入
        monitorUserApiInfoEventTrigger.saveEvent(apiMonitorInfoDO);

        //写入t_api_user_statistics
        ApiStatisticsDO apiStatisticsDO = new ApiStatisticsDO();

        apiStatisticsDO
                .setPath(path)
                .setRequestMethod(pattern)
                .setAkId(akId)
                .setMaxTime(invokeTime)
                .setMinTime(invokeTime)
                .setTotalTime(invokeTime)
                .setTotalRequestCount(1L)
        ;
        //查询是否存在
        ApiStatisticsDO userStatisticsDO = monitorService.queryOne(apiStatisticsDO);
        if (userStatisticsDO == null) {
            insertStatisticsDO(apiStatisticsDO, resultVO);
        } else {
            updateStatisticsDO(resultVO, invokeTime, userStatisticsDO);
        }
    }

    /**
     * 插入OpenAPI统计信息表
     */
    private void insertStatisticsDO(ApiStatisticsDO apiStatisticsDO, ResultVO<Void> resultVO) {
        if (resultVO == null) {
            apiStatisticsDO.setSuccessCount(1L);
            apiStatisticsDO.setErrorCount(0L);
        } else {
            apiStatisticsDO.setErrorCount(1L);
            apiStatisticsDO.setSuccessCount(0L);
        }
        monitorApiEventTrigger.saveEvent(apiStatisticsDO);
    }

    /**
     * 更新OpenAPI统计信息表
     */
    private void updateStatisticsDO(ResultVO<Void> resultVO, Long invokeTime, ApiStatisticsDO userStatisticsDO) {
        //最大调用时长
        Long maxTime = MonitorUtil.storeMaxTime(userStatisticsDO.getMaxTime(), invokeTime);
        userStatisticsDO.setMaxTime(maxTime);

        //最短调用时长
        Long minTime = MonitorUtil.storeMinTime(userStatisticsDO.getMinTime(), invokeTime);
        userStatisticsDO.setMinTime(minTime);

        //总时长
        Long totalTime = MonitorUtil.totalTime(userStatisticsDO.getTotalTime(), invokeTime);
        userStatisticsDO.setTotalTime(totalTime);

        //获取总次数
        Long count = MonitorUtil.totalCount(userStatisticsDO.getTotalRequestCount());
        userStatisticsDO.setTotalRequestCount(count);

        //设置成功失败次数
        getStatusCount(resultVO, userStatisticsDO);

        //写入mysql
        monitorApiEventTrigger.updateEvent(userStatisticsDO);
    }

    private void getStatusCount(ResultVO<Void> resultVO, ApiStatisticsDO userStatisticsDO) {
        if (resultVO == null) {
            userStatisticsDO.setSuccessCount(userStatisticsDO.getSuccessCount() + 1);
        } else {
            userStatisticsDO.setErrorCount(userStatisticsDO.getErrorCount() + 1);
        }
    }


}
