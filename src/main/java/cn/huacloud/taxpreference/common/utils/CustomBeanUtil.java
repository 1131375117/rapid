package cn.huacloud.taxpreference.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 自定义bean工具类
 *
 * @author wangkh
 */
public class CustomBeanUtil {

    /**
     * bean对象属性拷贝
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyProperties(Object source, Object target) {
        copyProperties(source, target, true, false, null, null);
    }

    /**
     * bean对象属性拷贝
     * @param source 源对象
     * @param target 目标对象
     * @param ignoreEmpty 是否忽略空字符串和空集合
     */
    public static void copyProperties(Object source, Object target, boolean ignoreEmpty) {
        copyProperties(source, target, true, ignoreEmpty, null, null);
    }

    /**
     * 根据给定类型创建对象，并把属性拷贝到对象中
     * @param source 源对象
     * @param targetClass 目标对象类型
     * @return 拷贝属性的目标对象
     */
    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        try {
            Constructor<T> constructor = targetClass.getConstructor();
            T target = constructor.newInstance();
            copyProperties(source, target, false, false, null, null);
            return target;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(targetClass.getName() + "必须要有无参构造器");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void copyProperties(Object source, Object target, boolean ignoreNull, boolean ignoreEmpty,
                                       @Nullable Class<?> editable,
                                       @Nullable String... ignoreProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
                        "] not assignable to Editable class [" + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        ResolvableType sourceResolvableType = ResolvableType.forMethodReturnType(readMethod);
                        ResolvableType targetResolvableType = ResolvableType.forMethodParameter(writeMethod, 0);

                        // Ignore generic types in assignable check if either ResolvableType has unresolvable generics.
                        boolean isAssignable =
                                (sourceResolvableType.hasUnresolvableGenerics() || targetResolvableType.hasUnresolvableGenerics() ?
                                        ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType()) :
                                        targetResolvableType.isAssignableFrom(sourceResolvableType));

                        if (isAssignable) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(source);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                // ignore null
                                if (ignoreNull && value == null) {
                                    continue;
                                }
                                // ignore empty
                                if (ignoreEmpty && value instanceof CharSequence && StringUtils.isBlank((CharSequence) value)) {
                                    continue;
                                }
                                if (ignoreEmpty && value instanceof Collection && ((Collection<?>) value).isEmpty()) {
                                    continue;
                                }
                                writeMethod.invoke(target, value);
                            } catch (Throwable ex) {
                                throw new FatalBeanException(
                                        "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                            }
                        }
                    }
                }
            }
        }
    }

}
