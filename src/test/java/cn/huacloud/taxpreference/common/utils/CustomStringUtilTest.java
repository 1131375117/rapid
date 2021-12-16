package cn.huacloud.taxpreference.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CustomStringUtilTest {

    @Test
    public void testDisjoin() {
        List<String> one = new ArrayList<>();
        one.add("A");
        one.add("B");
        List<String> two = new ArrayList<>();
        two.add("B");
        boolean disjoint = Collections.disjoint(one, two);
    }
}