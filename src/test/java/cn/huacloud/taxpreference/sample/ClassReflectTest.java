package cn.huacloud.taxpreference.sample;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author wangkh
 */
@Slf4j
public class ClassReflectTest {

    @Test
    public void testStackTrace() {
        foo();
    }

    @Test
    public void testMethodName() {
        String invokeMethodName = getInvokeMethodName();
        log.info(invokeMethodName);
    }

    private void foo() {
        log.info("invoke foo");
        bar();
    }

    private void bar() {
        log.info("invoke bar");
        StackTraceElement[] stackTrace = getStackTrace();
        log.info("Stack trace element size: {}", stackTrace.length);
        StackTraceElement stackTraceElement = stackTrace[2];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        log.info("");

    }

    private StackTraceElement[] getStackTrace() {
        return Thread.currentThread().getStackTrace();
    }

    private String getInvokeMethodName() {
        StackTraceElement element = Thread.currentThread().getStackTrace()[2];
        return element.getClassName() + "." + element.getMethodName();
    }

}
