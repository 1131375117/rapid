package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.exception.TaxPreferenceException;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author wangkh
 */
@Slf4j
@RestControllerAdvice
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
                resultVO = BizCode._4211.getResultVO();
            } else {
                resultVO = BizCode._4200.getResultVO();
            }
        } else if (e instanceof NotRoleException) {
            resultVO = BizCode._4201.getResultVO(e.getMessage());
        } else if (e instanceof NotPermissionException) {
            resultVO = BizCode._4203.getResultVO(e.getMessage());
        } else {
            resultVO = BizCode._500.getResultVO();
            resultVO.setData(e.getMessage());
        }
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
        log.info("接口调用业务异常", e);
        return new ResultVO<>(e.getCode(), e.getMessage(), e.getData());
    }

    /**
     * 捕获参数校验异常
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
        return new ResultVO<>(BizCode._4100.code, message, null);
    }

    /**
     * 全局异常
     * @return resultVO
     */
    @ExceptionHandler(Exception.class)
    public ResultVO<Void> handleException(Exception e) {
        log.error("接口调用异常", e);
        return BizCode._500.getResultVO();
    }
}
