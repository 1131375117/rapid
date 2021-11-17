package cn.huacloud.taxpreference.sample;

import cn.huacloud.taxpreference.services.consumer.impl.FrequentlyAskedQuestionSearchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 反射测试工具类
 * @author wangkh
 */
@Slf4j
public class ReflectTest {

    @Test
    public void getSuperGenericClass() {
        FrequentlyAskedQuestionSearchServiceImpl impl = new FrequentlyAskedQuestionSearchServiceImpl(null, null);
        Class<?> returnClass = impl.getResultClass();
        log.info(returnClass.getName());
    }
}
