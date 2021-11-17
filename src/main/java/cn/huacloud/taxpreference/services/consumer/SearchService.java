package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.RangeField;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.dtos.RangeQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.AbstractHighlightPageQueryDTO;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;
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
        List<R> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            R result = mapSearchHit(hit, pageQuery.searchFields());
            records.add(result);
        }

        // 返回分页对象
        return new PageVO<R>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
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
    default QueryBuilder getQueryBuilder(T pageQuery) {
        BoolQueryBuilder queryBuilder = generatorDefaultQueryBuilder(pageQuery);

        // 关键字查询
        String keyword = pageQuery.getKeyword();
        if (keyword != null) {
            List<String> searchFields = pageQuery.searchFields();
            for (String searchField : searchFields) {
                queryBuilder.should(matchPhraseQuery(searchField, keyword));
            }
        }

        return queryBuilder;
    }

    /**
     * 获取高亮构造器
     *
     * @param pageQuery 分页检索条件
     * @return 高亮构造器
     */
    default HighlightBuilder getHighlightBuilder(T pageQuery) {
        HighlightBuilder highlightBuilder = new HighlightBuilder();

        highlightBuilder.preTags("<span style=\"color:#006EFF;\">");
        highlightBuilder.postTags("</span>");
        List<String> searchFields = pageQuery.searchFields();
        if (CollectionUtils.isEmpty(searchFields)) {
            return null;
        }
        // 不需要截取的高亮字段，例如 title name之类的字段
        Set<String> notFragmentHighlightFields = pageQuery.notFragmentHighlightFields();
        for (String highlightField : searchFields) {
            // 判断不需要截取的高亮字段
            if (notFragmentHighlightFields.contains(highlightField)) {
                highlightBuilder.field(highlightField, -1, 0);
            } else {
                highlightBuilder.field(highlightField);
            }
        }
        return highlightBuilder;
    }

    RestHighLevelClient getRestHighLevelClient();

    /**
     * 映射搜索结果
     * @param searchHit 搜索结果
     */
    default R mapSearchHit(SearchHit searchHit, List<String> searchFields) throws Exception {
        R result = getObjectMapper().readValue(searchHit.getSourceAsString(), getResultClass());
        for (String searchField : searchFields) {
            PropertyDescriptor pd = new PropertyDescriptor(searchField, getResultClass());
            pd.getWriteMethod().invoke(result, getHighlightString(searchHit, searchField));
        }
        return result;
    }

    /**
     * 获取检索返回结果类型
     * @return 返回结果类型
     */
    Class<R> getResultClass();

    ObjectMapper getObjectMapper();

    default String getHighlightString(SearchHit searchHit, String key) {
        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
        String sourceValue = (String) sourceAsMap.get(key);
        Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
        if (highlightFields == null) {
            return sourceValue;
        }
        HighlightField highlightField = highlightFields.get(key);
        if (highlightField == null) {
            return sourceValue;
        }
        return highlightField.getFragments()[0].string();
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

    /**
     * 格式化Wildcard查询关键字
     * @param value 关键字
     * @return 格式化后的查询关键字
     */
    default String formatWildcardValue(String value) {
        Assert.notNull(value, "Wildcard value could not be null.");
        return "*" + value + "*";
    }

    default SearchResponse simplePageSearch(String index, QueryBuilder queryBuilder, PageQueryDTO pageQuery, FieldSortBuilder ... fieldSortBuilders) throws Exception {
        // 构建查询条件
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .trackTotalHits(true)
                .query(queryBuilder)
                .from(pageQuery.from())
                .size(pageQuery.getPageSize());

        // 设置排序
        for (FieldSortBuilder fieldSortBuilder : fieldSortBuilders) {
            searchSourceBuilder.sort(fieldSortBuilder);
        }

        SearchRequest request = new SearchRequest(index);
        request.source(searchSourceBuilder);

        // 执行查询
        return getRestHighLevelClient().search(request, RequestOptions.DEFAULT);
    }

    @AllArgsConstructor
    @Data
    class FieldWrapper {
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
