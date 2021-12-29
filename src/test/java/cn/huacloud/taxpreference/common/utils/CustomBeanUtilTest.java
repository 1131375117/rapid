package cn.huacloud.taxpreference.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class CustomBeanUtilTest {

    @Test
    public void testCopy() {
        Bar bar = CustomBeanUtil.copyProperties(new Foo().setName("AA"), Bar.class);
        log.info("{}", bar);
    }

    @Test
    public void testPd() {

    }

    @Accessors(chain = true)
    @Data
    public static class Foo {
        private String name;

        private String foo;
    }

    @Accessors(chain = true)
    @Data
    public static class Bar {
        private String name;
        private String bar;
    }

    @Test
    public void copyProperties() {
        Peach greenPeach = new Peach(null, "green");
        Peach redPeach = new Peach("红桃子", "红色");
        CustomBeanUtil.copyProperties(greenPeach, redPeach);
        Peach peach = CustomBeanUtil.copyProperties(redPeach, Peach.class);
        log.info("{}", peach);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Peach {
        private String name;
        private String color;
    }
}