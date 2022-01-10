package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.RangeField;
import cn.huacloud.taxpreference.common.annotations.WithChildrenCodes;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.dtos.RangeQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.AbstractHighlightPageQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PreviousNextVO;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.lucene.search.TotalHits;
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
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
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
                .fetchSource(null, getExcludeSource())
                .highlighter(highlightBuilder)
                .from(pageQuery.from())
                .size(pageQuery.getPageSize());

        // 添加排序字段
        for (SortBuilder<?> sortBuilder : pageQuery.sortBuilders()) {
            searchSourceBuilder.sort(sortBuilder);
        }
        // 创建查询请求
        SearchRequest searchRequest = new SearchRequest(pageQuery.index());
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
     * 获取查询构造器，实现类用来重写用的
     *
     * @param pageQuery 分页检索条件
     * @return 查询构造器
     */
    default QueryBuilder getQueryBuilder(T pageQuery) {
        return getDefaultQueryBuilder(pageQuery);
    }

    default BoolQueryBuilder getDefaultQueryBuilder(T pageQuery) {
        BoolQueryBuilder queryBuilder = generatorDefaultQueryBuilder(pageQuery);
        // 关键字查询
        List<String> keywordSplit = pageQuery.getKeywordSplit();
        if (keywordSplit != null) {
            BoolQueryBuilder keywordSplitQuery = boolQuery();
            for (String keyword : keywordSplit) {
                BoolQueryBuilder keywordQuery = boolQuery();
                List<String> searchFields = pageQuery.searchFields();
                for (String searchField : searchFields) {
                    if (pageQuery.getPreciseQuery()) {
                        // 精确查询
                        keywordQuery.should(matchPhraseQuery(searchField, keyword));
                    } else {
                        // 模糊查询
                        keywordQuery.should(matchQuery(searchField, keyword));
                    }
                }
                keywordSplitQuery.must(keywordQuery);
            }
            queryBuilder.must(keywordSplitQuery);
        }
        return queryBuilder;
    }

    /**
     * 获取排除不获取的字段
     *
     * @return 排除不获取的字段
     */
    default String[] getExcludeSource() {
        return new String[]{"content"};
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
     *
     * @param searchHit 搜索结果
     */
    default R mapSearchHit(SearchHit searchHit, List<String> searchFields) throws Exception {
        R result = getObjectMapper().readValue(searchHit.getSourceAsString(), getResultClass());
        for (String searchField : searchFields) {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(getResultClass(), searchField);
            if (propertyDescriptor == null) {
                continue;
            }
            propertyDescriptor.getWriteMethod().invoke(result, getHighlightString(searchHit, searchField));
        }
        return result;
    }

    /**
     * 获取检索返回结果类型
     *
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
     *
     * @param pageQuery
     * @return
     */
    default BoolQueryBuilder generatorDefaultQueryBuilder(T pageQuery) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        // 获取所有父类类型
        List<Class<?>> classList = new ArrayList<>();
        Class<?> targetClass = pageQuery.getClass();
        while (!targetClass.isAssignableFrom(Object.class)) {
            classList.add(targetClass);
            targetClass = targetClass.getSuperclass();
        }
        List<FieldWrapper> fieldWrappers = classList.stream()
                .flatMap(clazz -> Arrays.stream(clazz.getDeclaredFields()))
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
     *
     * @param value 关键字
     * @return 格式化后的查询关键字
     */
    default String formatWildcardValue(String value) {
        Assert.notNull(value, "Wildcard value could not be null.");
        return "*" + value + "*";
    }

    /**
     * 简单分页搜索
     *
     * @param index             索引/别名
     * @param queryBuilder      查询构建器
     * @param pageQuery         分页参数
     * @param fieldSortBuilders 排序构建器
     * @return 查询响应
     */
    default SearchResponse simplePageSearch(String index, QueryBuilder queryBuilder, PageQueryDTO pageQuery, FieldSortBuilder... fieldSortBuilders) throws Exception {
        // 参数合理化
        pageQuery.paramReasonable();
        // 构建查询条件
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .trackTotalHits(true)
                .query(queryBuilder)
                .from(pageQuery.from())
                .fetchSource(null, getExcludeSource())
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

    /**
     * 获取默认的上一篇和下一篇
     * 索引数据必须要有 id 字段且为long类型
     *
     * @param index 索引名称
     * @param id    主键ID
     * @return 上一篇、下一篇视图
     */
    default PreviousNextVO<Long> getDefaultPreviousNext(String index, Long id) throws Exception {

        PreviousNextVO<Long> previousNextVO = new PreviousNextVO<>();

        String field = "id";

        String[] includes = {"id", "title"};

        // previous
        SearchRequest previousRequest = new SearchRequest(index)
                .source(SearchSourceBuilder.searchSource()
                        .size(1)
                        .fetchSource(includes, null)
                        .query(rangeQuery(field).gt(id))
                        .sort(field, SortOrder.ASC));

        SearchResponse previousResponse = getRestHighLevelClient().search(previousRequest, RequestOptions.DEFAULT);

        TotalHits previousTotalHits = previousResponse.getHits().getTotalHits();
        if (previousTotalHits.value > 0) {
            String sourceAsString = previousResponse.getHits().getHits()[0].getSourceAsString();
            PreviousNextVO.Doc<Long> doc = getObjectMapper().readValue(sourceAsString, new TypeReference<PreviousNextVO.Doc<Long>>() {
            });
            previousNextVO.setPrevious(doc);
        }

        // next
        SearchRequest nextRequest = new SearchRequest(index)
                .source(SearchSourceBuilder.searchSource()
                        .size(1)
                        .fetchSource(includes, null)
                        .query(rangeQuery(field).lt(id))
                        .sort(field, SortOrder.DESC));

        SearchResponse nextResponse = getRestHighLevelClient().search(nextRequest, RequestOptions.DEFAULT);

        TotalHits nextTotalHits = nextResponse.getHits().getTotalHits();
        if (nextTotalHits.value > 0) {
            String sourceAsString = nextResponse.getHits().getHits()[0].getSourceAsString();
            PreviousNextVO.Doc<Long> doc = getObjectMapper().readValue(sourceAsString, new TypeReference<PreviousNextVO.Doc<Long>>() {
            });
            previousNextVO.setNext(doc);
        }

        return previousNextVO;
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
            WithChildrenCodes withChildrenCodesAnnotation = fieldWrapper.getField().getAnnotation(WithChildrenCodes.class);
            // 设置过滤条件
            if (value instanceof Collection) {
                // 集合
                Collection<?> collection = (Collection<?>) value;
                // 空集合直接返回
                if (CollectionUtils.isEmpty(collection)) {
                    return;
                }
                // 是否包含叶子节点码值
                if (withChildrenCodesAnnotation != null) {
                    SysCodeService sysCodeService = SpringUtil.getBean(SysCodeService.class);
                    List<String> withChildrenCodes = sysCodeService.withChildrenCodes(withChildrenCodesAnnotation.value(), collection);
                    if (withChildrenCodes.isEmpty()) {
                        return;
                    }
                    boolQueryBuilder.must(termsQuery(name, withChildrenCodes));
                } else {
                    boolQueryBuilder.must(termsQuery(name, collection));
                }
            } else if (value instanceof IEnum) {
                // IEnum枚举
                IEnum<?> iEnum = (IEnum<?>) value;
                boolQueryBuilder.must(matchQuery(name, iEnum.getValue()));
            } else if (value instanceof String) {
                // 是否包含叶子节点码值
                if (withChildrenCodesAnnotation != null) {
                    SysCodeService sysCodeService = SpringUtil.getBean(SysCodeService.class);
                    List<String> withChildrenCodes = sysCodeService.withChildrenCodes(withChildrenCodesAnnotation.value(), Collections.singleton(value));
                    if (withChildrenCodes.isEmpty()) {
                        return;
                    }
                    boolQueryBuilder.must(termsQuery(name, withChildrenCodes));
                } else {
                    boolQueryBuilder.must(matchQuery(name, value));
                }
            } else if (value instanceof Number) {
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
