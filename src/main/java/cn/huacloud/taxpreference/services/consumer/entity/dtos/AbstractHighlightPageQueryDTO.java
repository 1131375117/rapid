package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.utils.StringUtils;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
        Environment environment = StringUtils.getBean(Environment.class);
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
     * 排序构建器
     * @return 排序构建器
     */
    public List<SortBuilder<?>> sortBuilders() {
        return new ArrayList<>();
    }

}
