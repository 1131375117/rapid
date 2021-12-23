package cn.huacloud.taxpreference.sample;

import cn.huacloud.taxpreference.services.consumer.entity.dtos.DynamicConditionQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.DynamicConditionVO;
import cn.huacloud.taxpreference.services.consumer.impl.FrequentlyAskedQuestionSearchServiceImpl;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射测试工具类
 * @author wangkh
 */
@Slf4j
public class ReflectTest {


    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testPD() throws Exception {
        DynamicConditionVO dynamicConditionVO = new DynamicConditionVO();
        //                 setEnterpriseTypes
        dynamicConditionVO.setEnterpriseTypes(null);
        // PropertyDescriptor propertyDescriptor = new PropertyDescriptor("enterpriseTypes", DynamicConditionVO.class);
        PropertyDescriptor pd = new PropertyDescriptor("name", Fruit.class);
    }

    @Test
    public void testGetSuperClass() {
        Class<? super Fruit> superclass = Fruit.class.getSuperclass();
        List<Class<?>> classList = new ArrayList<>();
        Class<?> target = DynamicConditionQueryDTO.class;
        while (!target.isAssignableFrom(Object.class)) {
            classList.add(target);
            target = target.getSuperclass();
        }
        log.info("{}", classList);
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

    @Data
    static class Fruit implements UUIDGetter {
        private String name;
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
