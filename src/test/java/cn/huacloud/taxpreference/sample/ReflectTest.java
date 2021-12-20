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
        // true
        boolean one = Fruit.class.isAssignableFrom(Apple.class);
        // false
        boolean two = Apple.class.isAssignableFrom(Fruit.class);
        // true
        boolean three = Apple.class.isAssignableFrom(Apple.class);
        log.info("one:{}, two:{}, three:{}", one, two, three);
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
