package cn.huacloud.taxpreference.sync.spider.processor;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * 日期处理器
 * @author wangkh
 */
public interface DateProcessors {

    /**
     * 各类日期formatter集合
     */
    DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

    /**
     * 发布日期处理器
     */
    Function<String, LocalDate> releaseDate = text -> {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(text, formatter);
            } catch (Exception e) {
                // do nothing
            }
        }
        return null;
    };
}
