package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.spring.SpringMVCUtil;
import cn.huacloud.taxpreference.common.constants.TaxBaseConstants;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.openapi.auth.OpenApiStpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author wangkh
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {


    /**
     * 捕获 sa-token 权限异常
     *
     * @param e 被捕获异常
     * @return resultVO
     */
    @ExceptionHandler(SaTokenException.class)
    public ResultVO handleSaTokenException(SaTokenException e) {
        ResultVO resultVO;
        if (e instanceof NotLoginException) {
            NotLoginException notLoginException = (NotLoginException) e;
            if (NotLoginException.TOKEN_TIMEOUT.equals(notLoginException.getType())) {
                if (OpenApiStpUtil.getLoginType().equals(notLoginException.getLoginType())) {
                    resultVO = BizCode._4703.getResultVO();
                } else {
                    resultVO = BizCode._4211.getResultVO();
                }
            } else {
                if (OpenApiStpUtil.getLoginType().equals(notLoginException.getLoginType())) {
                    resultVO = BizCode._4702.getResultVO();
                } else {
                    resultVO = BizCode._4200.getResultVO();
                }
            }
        } else if (e instanceof NotRoleException) {
            resultVO = BizCode._4201.getResultVO(e.getMessage());
        } else if (e instanceof NotPermissionException) {
            resultVO = BizCode._4202.getResultVO(e.getMessage());
        } else {
            resultVO = BizCode._500.getResultVO();
            resultVO.setData(e.getMessage());
        }
        SpringMVCUtil.getRequest().setAttribute(TaxBaseConstants.REQUEST_KEY, resultVO);
        return resultVO;
    }

    /**
     * 捕获系统业务异常
     *
     * @param e 被捕获异常
     * @return resultVO
     */
    @ExceptionHandler(TaxPreferenceException.class)
    public ResultVO<Object> handleTaxPreferenceException(TaxPreferenceException e) {
        log.info("接口调用业务异常: {}", e.getMessage());
        SpringMVCUtil.getRequest().setAttribute(TaxBaseConstants.REQUEST_KEY, new ResultVO<>(e.getCode(), e.getMessage(), e.getData()));
        return new ResultVO<>(e.getCode(), e.getMessage(), e.getData());
    }

    /**
     * 捕获参数校验异常
     *
     * @param e 被捕获异常
     * @return resultVO
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 封装参数校验信息
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + " => " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(";"));
        log.info("参数校验失败：{}", message);
        SpringMVCUtil.getRequest().setAttribute(TaxBaseConstants.REQUEST_KEY, new ResultVO<>(BizCode._4100.code, message, null));

        return new ResultVO<>(BizCode._4100.code, message, null);
    }

    /**
     * 消息转换异常，通常是前端入参错误，提示前端
     *
     * @param e 被捕获异常
     * @return resultVO
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResultVO<Void> handleHttpMessageConversionException(HttpMessageConversionException e) {
        log.error("消息转换异常", e);
        SpringMVCUtil.getRequest().setAttribute(TaxBaseConstants.REQUEST_KEY, BizCode._4100.getResultVO());
        return BizCode._4100.getResultVO();
    }

    /**
     * 全局异常
     *
     * @return resultVO
     */
    @ExceptionHandler(Exception.class)
    public ResultVO<Void> handleException(Exception e) {
        log.error("接口调用异常", e);
        SpringMVCUtil.getRequest().setAttribute(TaxBaseConstants.REQUEST_KEY, BizCode._500.getResultVO());
        return BizCode._500.getResultVO();
    }


}
