package cn.huacloud.taxpreference.sample;

import cn.dev33.satoken.secure.SaSecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * 简单测试类
 * @author wangkh
 */
@Slf4j
public class SampleTest {

    @Test
    public void testReflectMethod() {
        Method method = this.getClass().getDeclaredMethods()[0];
        log.info(method.toString());
        log.info(method.toGenericString());
    }

    @Test
    public void md5Test() {
        String md5 = SaSecureUtil.md5("123456");
        log.info(md5);
    }

    @Test
    public void testLogMessageFormat() {
        log.info("{}和{}是好友。", "小明", "小红");
    }
}
