package cn.huacloud.taxpreference.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 税收优惠综合工具类
 * @author wangkh
 */
public class TaxPreferenceUtil {

    /**
     * 给","分隔的字符串添加新元素
     * @param target 目标字符串
     * @param element 新元素
     * @return 合并后的字符串
     */
    public static String separatorStrAddElement(String target, String element) {
        if (StringUtils.isBlank(target)) {
            return element;
        }
        List<String> targetList = Arrays.asList(target.split(target));
        Set<String> treeSet = new TreeSet<>(targetList);
        treeSet.add(element);
        return String.join(",", treeSet);
    }
}
