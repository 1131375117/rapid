package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.ExSearchQueryDTO;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.*;

/**
 * @author wangkh
 */
public abstract class AbstractHighlightPageQueryDTO extends ExSearchQueryDTO {

    /**
     * 搜索索引范围
     * @return 搜索索引范围
     */
    public String index() {
        ElasticsearchIndexConfig elasticsearchIndexConfig = SpringUtil.getBean(ElasticsearchIndexConfig.class);
        return index(elasticsearchIndexConfig);
    }

    public abstract String index(ElasticsearchIndexConfig config);

    /**
     * 获取高亮字段
     * @return 高亮字段集合
     */
    public List<String> searchFields() {
        switch (getSearchScope()) {
            case CONTENT:
                return Collections.singletonList("combinePlainContent");
            case TITLE_AND_CONTENT:
                return Arrays.asList("title", "combinePlainContent");
            default:
                return Collections.singletonList("title");
        }
    }

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
        if (StringUtils.isBlank(getKeyword())) {
            return Collections.singletonList(SortBuilders.fieldSort("_id").order(SortOrder.DESC));
        }
        return new ArrayList<>();
    }

    @Override
    public void
    paramReasonable() {
        super.paramReasonable();
        stringParamNullOrTrim();
    }
}
