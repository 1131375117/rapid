package cn.huacloud.taxpreference.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class CustomBeanUtilTest {

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