package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.AbstractHighlightPageQueryDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangkh
 */
public interface SearchService<T extends AbstractHighlightPageQueryDTO, R> {

    /**
     * 分页检索
     *
     * @param pageQuery 分页检索条件
     * @return 分页查询结果
     */
    default PageVO<R> pageSearch(T pageQuery) throws Exception {
        // 参数合理化
        pageQuery.paramReasonable();
        // 查询条件前置处理
        preParamProcessing(pageQuery);
        // 获取查询构造器
        QueryBuilder queryBuilder = getQueryBuilder(pageQuery);
        // 获取高亮构造器
        HighlightBuilder highlightBuilder = getHighlightBuilder(pageQuery);
        // 创建搜索内容构造器
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .trackTotalHits(true)
                .query(queryBuilder)
                .highlighter(highlightBuilder)
                .from(pageQuery.from())
                .size(pageQuery.getPageSize());
        // 添加排序字段
        for (SortBuilder<?> sortBuilder : pageQuery.sortBuilders()) {
            searchSourceBuilder.sort(sortBuilder);
        }
        // 创建查询请求
        SearchRequest searchRequest = new SearchRequest(pageQuery.indices());
        searchRequest.source(searchSourceBuilder);
        // 执行查询
        SearchResponse response = getRestHighLevelClient().search(searchRequest, RequestOptions.DEFAULT);

        // 封装数据记录
        SearchHits hits = response.getHits();
        List<R> recodes = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            R result = mapSearchHit(hit);
            recodes.add(result);
        }

        // 返回分页对象
        return new PageVO<R>()
                .setTotal(hits.getTotalHits())
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(recodes);
    }

    /**
     * 查询条件前置处理
     *
     * @param pageQuery 分页检索条件
     */
    default void preParamProcessing(T pageQuery) {
    }

    /**
     * 获取查询构造器
     *
     * @param pageQuery 分页检索条件
     * @return 查询构造器
     */
    QueryBuilder getQueryBuilder(T pageQuery);

    /**
     * 获取高亮构造器
     *
     * @param pageQuery 分页检索条件
     * @return 高亮构造器
     */
    default HighlightBuilder getHighlightBuilder(T pageQuery) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        highlightBuilder.preTags();
        highlightBuilder.postTags();
        List<String> searchFields = pageQuery.searchFields();
        if (CollectionUtils.isEmpty(searchFields)) {
            return null;
        }
        for (String highlightField : searchFields) {
            highlightBuilder.field(highlightField, -1, -1, -1);
        }
        return highlightBuilder;
    }

    RestHighLevelClient getRestHighLevelClient();

    R mapSearchHit(SearchHit searchHit) throws Exception;

    default String getHighlightString(SearchHit searchHit, String key) {
        return searchHit.getHighlightFields().get(key).getFragments()[0].string();
    }
}
