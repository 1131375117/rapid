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
    public void testAssignableFrom() {
        boolean one = Fruit.class.isAssignableFrom(Apple.class);
        boolean two = Apple.class.isAssignableFrom(Fruit.class);
        boolean three = Apple.class.isAssignableFrom(Apple.class);
        log.info("one:{}, two:{}, three{}", one, two, three);
    }

    static class Fruit {

    }

    static class Apple extends Fruit {

    }

    static class Peach extends Fruit {

    }

    @Test
    public void getSuperGenericClass() {
        FrequentlyAskedQuestionSearchServiceImpl impl = new FrequentlyAskedQuestionSearchServiceImpl(null, null);
        Class<?> returnClass = impl.getResultClass();
        log.info(returnClass.getName());
    }
}
