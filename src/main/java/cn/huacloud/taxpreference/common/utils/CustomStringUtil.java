package cn.huacloud.taxpreference.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义字符串工具类
 * @author wangkh
 */
public class CustomStringUtil {

    public static Set<String> spiltStringToSet(String target) {
        return new LinkedHashSet<>(spiltStringToList(target));
    }

    public static List<String> spiltStringToList(String target) {
        if (StringUtils.isBlank(target)) {
            return new ArrayList<>();
        }
        return Arrays.stream(target.split(",")).collect(Collectors.toList());
    }
}
