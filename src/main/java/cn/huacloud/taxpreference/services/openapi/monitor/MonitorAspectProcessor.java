package cn.huacloud.taxpreference.services.openapi.monitor;

import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.openapi.auth.OpenApiStpUtil;
import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiMonitorInfoDO;
import cn.huacloud.taxpreference.services.openapi.message.OpenApiMessageSender;
import cn.huacloud.taxpreference.services.wework.support.ObjectMapperProvider;
import cn.hutool.core.date.DateUtil;
import com.ctc.wstx.util.DataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * OpenAPI监控切面处理器
 * @author wangkh
 */
@Aspect
@Component
public class MonitorAspectProcessor {

    private OpenApiMessageSender openApiMessageSender;

    private ObjectMapper objectMapper;

    @Autowired
    public void setOpenApiMessageSender(OpenApiMessageSender openApiMessageSender) {
        this.openApiMessageSender = openApiMessageSender;
    }
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 环绕通知
     */
    @Around("@annotation(cn.huacloud.taxpreference.services.openapi.monitor.MonitorApi)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 秒表
        long startTimeMillions = 0;
        long endTimeMillions = 0;
        Object result = null;
        Throwable exception = null;
        try {
            // 开始计时
            startTimeMillions = System.currentTimeMillis();

            // 执行调用
            result = joinPoint.proceed();

            // 结束计时
            endTimeMillions = System.currentTimeMillis();

            return result;
        } catch (Throwable e) {
            // 结束计时
            endTimeMillions = System.currentTimeMillis();
            exception = e;
            throw e;
        } finally {
            // 保存调用记录
            sendMonitorInfo(joinPoint, startTimeMillions, endTimeMillions, result, exception);
        }
    }

    private void sendMonitorInfo(ProceedingJoinPoint joinPoint, long startTimeMillions, long endTimeMillions, Object result, Throwable e) {
        HttpServletRequest httpServletRequest = SpringMVCUtil.getRequest();
        // akId
        String akId = (String) OpenApiStpUtil.getLoginIdDefaultNull();
        // API名称
        String apiName = getApiName(joinPoint);
        // 请求方式
        String method = httpServletRequest.getMethod();
        // 请求uri
        String uri = httpServletRequest.getRequestURI();
        // openUserId
        String openUserId = OpenApiStpUtil.getOpenUserId();
        // 请求参数
        String requestParams = getRequestParams(joinPoint);
        // 调用时间
        long invokeTime = endTimeMillions - startTimeMillions;

        // 调用状态
        int invokeStatus;
        // 调用消息
        String invokeMessage;
        if (result instanceof ResultVO) {
            ResultVO<?> resultVO = (ResultVO<?>) result;
            invokeStatus = resultVO.getCode();
            invokeMessage = resultVO.getMsg();
        } else if (e != null) {
            // 异常情况
            if (e instanceof TaxPreferenceException) {
                TaxPreferenceException taxPreferenceException = (TaxPreferenceException) e;
                invokeStatus = taxPreferenceException.getCode();
                invokeMessage = taxPreferenceException.getMessage();
            } else {
                // 这里可以添加其他异常支持
                invokeStatus = 500;
                invokeMessage = e.getMessage();
            }
        } else {
            // 未匹配的返回结果
            invokeStatus = 200;
            invokeMessage = "OK";
        }

        // 创建对象设置属性
        ApiMonitorInfoDO apiMonitorInfoDO = new ApiMonitorInfoDO()
                .setAkId(akId)
                .setOpenUserId(openUserId)
                .setApiName(apiName)
                .setRequestMethod(method)
                .setPath(uri)
                .setRequestParams(requestParams)
                .setStartTime(toLocalDateTime(startTimeMillions))
                .setEndTime(toLocalDateTime(endTimeMillions))
                .setInvokeTime(invokeTime)
                .setInvokeStatus("" + invokeStatus)
                .setInvokeMsg(invokeMessage);

        // 发送消息
        openApiMessageSender.sendApiMonitorInfo(apiMonitorInfoDO);
    }

    private LocalDateTime toLocalDateTime(long timeMillions) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillions), ZoneId.systemDefault());
    }

    private String getApiName(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return null;
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        ApiOperation annotation = methodSignature.getMethod().getAnnotation(ApiOperation.class);
        if (annotation == null) {
            return null;
        }
        return annotation.value();
    }

    @SneakyThrows
    private String getRequestParams(ProceedingJoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return null;
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Class[] parameterTypes = methodSignature.getParameterTypes();
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        List<ParamWrapper> paramWrapperList = new ArrayList<>();
        for (int i = 0; i < parameterNames.length && i < args.length; i++) {
            ParamWrapper paramWrapper = new ParamWrapper(parameterTypes[i], parameterNames[i], args[i]);
            paramWrapperList.add(paramWrapper);
        }

        Map<String, Object> params = paramWrapperList.stream()
                .filter(MonitorAspectProcessor::includeParam)
                .collect(Collectors.toMap(ParamWrapper::getName, ParamWrapper::getValue));
        if (params.isEmpty()) {
            return null;
        }
        String json = objectMapper.writeValueAsString(params);
        if (json.length() > 2000) {
            ObjectNode objectNode = objectMapper.createObjectNode().put("msg", "请求参数过长")
                    .put("data", json.substring(0, 100));
            return objectNode.toString();
        }
        return json;
    }

    @AllArgsConstructor
    @Data
    public static class ParamWrapper {
        private Class<?> type;
        private String name;
        private Object value;
    }

    private static final Class<?>[] excludeClass = {ServletRequest.class, ServletRequest.class, MultipartFile.class, Map.class};

    public static boolean includeParam(ParamWrapper paramWrapper) {
        for (Class<?> clazz : excludeClass) {
            boolean assignableFrom = clazz.isAssignableFrom(paramWrapper.getType());
            if (assignableFrom) {
                return false;
            }
        }
        return true;
    }
}
