package cn.huacloud.taxpreference.sample;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 简单测试类
 * @author wangkh
 */
@Slf4j
public class SampleTest {

    @Test
    public void testStringJoin() {
        String join = String.join(",", new ArrayList<>());
        log.info("join: '{}'", join);
    }

    @Test
    public void testIdWorker() {
        for (int i = 0; i < 100; i++) {
            log.info(IdWorker.get32UUID());
        }
    }

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
