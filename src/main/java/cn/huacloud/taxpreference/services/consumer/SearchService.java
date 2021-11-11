package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.RangeField;
import cn.huacloud.taxpreference.common.entity.dtos.RangeQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.AbstractHighlightPageQueryDTO;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

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
                .setTotal(hits.getTotalHits().value)
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
            highlightBuilder.field(highlightField, -1, 0);
        }
        return highlightBuilder;
    }

    RestHighLevelClient getRestHighLevelClient();

    R mapSearchHit(SearchHit searchHit) throws Exception;

    default String getHighlightString(SearchHit searchHit, String key) {
        return searchHit.getHighlightFields().get(key).getFragments()[0].string();
    }

    List<FieldHandler> fieldHandlers = Arrays.asList(new FilterFieldHandler(), new RangeFiledHandler());

    /**
     * 通过PageQuery的字段注解，自动生成BoolQueryBuilder
     * 目前支持过滤字段和范围字段
     * @param pageQuery
     * @return
     */
    default BoolQueryBuilder generatorDefaultQueryBuilder(T pageQuery) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        Class<?> clazz = pageQuery.getClass();
        List<FieldWrapper> fieldWrappers = Arrays.stream(clazz.getDeclaredFields())
                .map(field -> {
                    try {
                        field.setAccessible(true);
                        Object value = field.get(pageQuery);
                        return new FieldWrapper(field, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

        for (FieldWrapper fieldWrapper : fieldWrappers) {
            for (FieldHandler fieldHandler : fieldHandlers) {
                if (fieldHandler.supported(fieldWrapper.getField())) {
                    fieldHandler.apply(boolQueryBuilder, fieldWrapper);
                    break;
                }
            }
        }
        return boolQueryBuilder;
    }

    @AllArgsConstructor
    @Data
    static class FieldWrapper {
        private Field field;
        private Object value;
    }

    interface FieldHandler {

        boolean supported(Field field);

        void apply(BoolQueryBuilder boolQueryBuilder, FieldWrapper fieldWrapper);

    }

    class FilterFieldHandler implements FieldHandler {

        @Override
        public boolean supported(Field field) {
            return field.isAnnotationPresent(FilterField.class);
        }

        @Override
        public void apply(BoolQueryBuilder boolQueryBuilder, FieldWrapper fieldWrapper) {
            Object value = fieldWrapper.getValue();
            // 空值直接返回
            if (value == null) {
                return;
            }
            // 获取字段名称
            FilterField annotation = fieldWrapper.getField().getAnnotation(FilterField.class);
            String name = annotation.value();
            // 设置过滤条件
            if (value instanceof Collection) {
                // 集合
                Collection<?> collection = (Collection<?>) value;
                // 空集合直接返回
                if (collection.isEmpty()) {
                    return;
                }
                boolQueryBuilder.must(termsQuery(name, collection));
            } else if (value instanceof IEnum) {
                // IEnum枚举
                IEnum<?> iEnum = (IEnum<?>) value;
                boolQueryBuilder.must(matchQuery(name, iEnum.getValue()));
            } else if (value instanceof String) {
                boolQueryBuilder.must(matchQuery(name, value));
            } else {
                throw new RuntimeException("未支持的数据类型：" + value.getClass().getName());
            }
        }
    }

    class RangeFiledHandler implements FieldHandler {

        @Override
        public boolean supported(Field field) {
            return field.isAnnotationPresent(RangeField.class);
        }

        @Override
        public void apply(BoolQueryBuilder boolQueryBuilder, FieldWrapper fieldWrapper) {
            Object value = fieldWrapper.getValue();
            // 空值直接返回
            if (value == null) {
                return;
            }
            // 获取字段名称
            RangeField annotation = fieldWrapper.getField().getAnnotation(RangeField.class);
            String name = annotation.value();
            // 设置过滤条件
            if (value instanceof RangeQueryDTO) {
                RangeQueryDTO<?> rangeQueryDTO = (RangeQueryDTO<?>) value;
                Object from = rangeQueryDTO.getFrom();
                Object to = rangeQueryDTO.getTo();
                if (from == null && to == null) {
                    return;
                }
                RangeQueryBuilder rangeQueryBuilder = rangeQuery(name);
                if (from != null) {
                    rangeQueryBuilder.from(from.toString());
                }
                if (to != null) {
                    rangeQueryBuilder.to(to.toString());
                }
                boolQueryBuilder.must(rangeQueryBuilder);
            } else {
                throw new RuntimeException("@RangField只能注解在RangeQueryDTO类型的字段");
            }
        }
    }
}
