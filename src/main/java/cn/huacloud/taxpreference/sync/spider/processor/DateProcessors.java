package cn.huacloud.taxpreference.sync.spider.processor;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

/**
 * @author wangkh
 */
public interface DateProcessors {

    DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    };

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
