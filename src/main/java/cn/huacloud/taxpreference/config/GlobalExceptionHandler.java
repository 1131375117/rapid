package cn.huacloud.taxpreference.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author wangkh
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获 sa-token 权限异常
     * @param e 被捕获异常
     * @return
     */
    @ExceptionHandler(SaTokenException.class)
    public ResultVO handlerSaTokenException(SaTokenException e) {
        ResultVO resultVO;
        if (e instanceof NotLoginException) {
            resultVO = BizCode._4200.getResultVO();
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
}
