package cn.huacloud.taxpreference.common.utils;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;

/**
 * 校验工具类，需要集成spring使用，使用spring mvc内置的校验对象，保持风格统一
 * @author wangkh
 */
@Component
public class ValidationUtil  {

    /**
     * spring mvc中的校验器
     */
    private static LocalValidatorFactoryBean validator;
    /**
     * 默认的MethodParameter，捕获异常并不需要method参数信息，所以设置一个默认的
     */
    private static MethodParameter defaultMethodParameter;

    public ValidationUtil(LocalValidatorFactoryBean validator) throws Exception {
        ValidationUtil.validator = validator;
        Method method = ValidationUtil.class.getDeclaredMethods()[0];
        defaultMethodParameter = new MethodParameter(method, 0);
    }

    /**
     * 手工校验对象
     * @param target 待校验对象
     * @param groups 校验分组
     * @throws MethodArgumentNotValidException 参数校验异常
     */
    public static void validate(Object target, Class<?> ... groups) throws MethodArgumentNotValidException {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "defaultObjectName");
        // 执行校验
        validator.validate(target, bindingResult, groups);

        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(defaultMethodParameter, bindingResult);
        }
    }
}
