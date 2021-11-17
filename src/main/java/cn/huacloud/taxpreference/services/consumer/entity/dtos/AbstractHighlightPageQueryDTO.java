package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.core.env.Environment;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
public abstract class AbstractHighlightPageQueryDTO extends KeywordPageQueryDTO {

    /**
     * 搜索索引范围
     * @return 搜索索引范围
     */
    public String[] indices() {
        Environment environment = SpringUtils.getBean(Environment.class);
        Optional<String> profile = Arrays.stream(environment.getActiveProfiles())
                // 只考虑开发和测试环境
                .filter(value -> "dev".equalsIgnoreCase(value) || "test".equalsIgnoreCase(value))
                .findFirst();
        List<String> indexList = indexList();
        if (profile.isPresent()) {
            indexList = indexList.stream()
                    .map(index -> index + "_" + profile.get())
                    .collect(Collectors.toList());
        }
        return indexList.toArray(new String[0]);
    }

    public abstract List<String> indexList();

    /**
     * 获取高亮字段
     * @return 高亮字段集合
     */
    public abstract List<String> searchFields();

    /**
     * 不需要截取的高亮字段，例如：title name之类希望完整展示的字段
     */
    public Set<String> notFragmentHighlightFields() {
        return Collections.singleton("title");
    }

    /**
     * 排序构建器
     * @return 排序构建器
     */
    public List<SortBuilder<?>> sortBuilders() {
        return new ArrayList<>();
    }

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(this);
                // 空字符串处理
                Class<?> type = field.getType();
                if (type.isAssignableFrom(String.class)) {
                    String str = (String) value;
                    if (StringUtils.isBlank(str)) {
                        field.set(this, null);
                    } else {
                        // 去除前后空格
                        field.set(this, str.trim());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
