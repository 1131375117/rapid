package cn.huacloud.taxpreference.sample;

import cn.huacloud.taxpreference.common.utils.ConsumerUserUtil;
import cn.huacloud.taxpreference.services.consumer.impl.FrequentlyAskedQuestionSearchServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 反射测试工具类
 * @author wangkh
 */
@Slf4j
public class ReflectTest {


    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testLogin() {
        boolean login = ConsumerUserUtil.isLogin();
    }

    @Test
    public void testUUIDGetter() throws Exception {
        Fruit fruit = new Fruit();
        String str = objectMapper.writeValueAsString(fruit);
        log.info(str);
    }

    interface UUIDGetter {
        default String getUUID() {
            return IdWorker.get32UUID();
        }
    }

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

    static class Fruit implements UUIDGetter {

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
