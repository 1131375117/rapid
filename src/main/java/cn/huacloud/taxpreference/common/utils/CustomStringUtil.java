package cn.huacloud.taxpreference.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义字符串工具类
 * @author wangkh
 */
@Slf4j
public class CustomStringUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final CollectionType LIST_TYPE = OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, String.class);

    public static Set<String> spiltStringToSet(String target) {
        return new LinkedHashSet<>(spiltStringToList(target));
    }

    public static List<String> spiltStringToList(String target) {
        if (StringUtils.isBlank(target)) {
            return new ArrayList<>();
        }
        return Arrays.stream(target.split(",")).collect(Collectors.toList());
    }

    public static List<String> arrayStringToList(String target) {
        try {
            return OBJECT_MAPPER.readValue(target, LIST_TYPE);
        } catch (JsonProcessingException e) {
            log.error("字符串反序列为List<String>失败", e);
            return new ArrayList<>();
        }
    }

    public static boolean haveIntersection(String canSpiltStr, Collection<String> collection) {
        return !Collections.disjoint(spiltStringToSet(canSpiltStr), collection);
    }
}
