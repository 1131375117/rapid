package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.GroupVO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.CustomStringUtil;
import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.consumer.TaxPreferenceSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.DynamicConditionQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.LatestTaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.TaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class TaxPreferenceSearchServiceImpl implements TaxPreferenceSearchService {

    @Getter
    private final RestHighLevelClient restHighLevelClient;
    @Getter
    private final ObjectMapper objectMapper;

    private final SysParamService sysParamService;

    @Override
    public List<HotLabelVO> hotLabels(Integer size) throws Exception {
        // term aggregation
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("hot_label")
                .field("labels")
                .size(size);
        // source builder
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .aggregation(aggregationBuilder)
                .size(0);
        // search request
        SearchRequest searchRequest = new SearchRequest(getIndex())
                .source(searchSourceBuilder);
        // do search
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // data mapping
        Aggregations aggregations = response.getAggregations();
        ParsedStringTerms hotLabelTerms = aggregations.get("hot_label");
        return hotLabelTerms.getBuckets().stream()
                .map(bucket -> new HotLabelVO().setLabelName(bucket.getKeyAsString())
                        .setHotScore(bucket.getDocCount()))
                .collect(Collectors.toList());
    }

    @Override
    public QueryBuilder getQueryBuilder(TaxPreferenceSearchQueryDTO pageQuery) {

        BoolQueryBuilder queryBuilder = getDefaultQueryBuilder(pageQuery);

        // 标签过滤
        /*String label = pageQuery.getLabel();
        if (label != null) {
            queryBuilder.must(wildcardQuery("labels", formatWildcardValue(label)));
        }*/

        // 动态筛选条件过滤
        if (pageQuery.getConditions() != null) {
            BoolQueryBuilder conditionsQuery = boolQuery();
            for (TaxPreferenceSearchQueryDTO.ConditionQuery condition : pageQuery.getConditions()) {
                conditionsQuery.must(boolQuery()
                        .should(boolQuery().must(nestedQuery("conditions", boolQuery()
                                .must(termQuery("conditions.conditionName", condition.getConditionName()))
                                .must(termsQuery("conditions.conditionValues", condition.getConditionValues())), ScoreMode.Avg)))
                        .should(boolQuery().mustNot(nestedQuery("conditions", termQuery("conditions.conditionName", condition.getConditionName()), ScoreMode.Avg))));
            }
            queryBuilder.must(conditionsQuery);
        }

        return queryBuilder;
    }

    @Override
    public TaxPreferenceSearchVO getTaxPreferenceDetails(Long id) throws Exception {
        GetRequest request = new GetRequest(getIndex());
        request.id(id.toString());
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        if (!response.isExists()) {
            throw BizCode._4500.exception();
        }
        TaxPreferenceSearchVO taxPreferenceSearchVO = objectMapper.readValue(response.getSourceAsString(), TaxPreferenceSearchVO.class);
        // 设置上一篇、下一篇
        PreviousNextVO<Long> defaultPreviousNext = getDefaultPreviousNext(getIndex(), id);
        taxPreferenceSearchVO.setPreviousNext(defaultPreviousNext);
        return taxPreferenceSearchVO;
    }

    @Override
    public PageVO<DocSearchSimpleVO> latestTaxPreference(LatestTaxPreferenceSearchQueryDTO pageQuery) throws Exception {
        // 构建查询条件
        QueryBuilder queryBuilder;
        if (pageQuery.getTaxCategoriesCode() == null) {
            queryBuilder = matchAllQuery();
        } else {
            queryBuilder = matchQuery("taxCategories.codeValue", pageQuery.getTaxCategoriesCode());
        }
        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                queryBuilder,
                pageQuery,
                SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC));

        // 数据映射
        SearchHits hits = response.getHits();
        List<DocSearchSimpleVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            DocSearchSimpleVO docSearchSimpleVO = objectMapper.readValue(hit.getSourceAsString(), DocSearchSimpleVO.class);
            records.add(docSearchSimpleVO);
        }

        // 返回数据
        return new PageVO<DocSearchSimpleVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    @Override
    public PageVO<DocSearchSimpleVO> hotTaxPreference(PageQueryDTO pageQuery) throws Exception {
        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                matchAllQuery(),
                pageQuery,
                SortBuilders.fieldSort("views").order(SortOrder.DESC),
                SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC));

        // 数据映射
        SearchHits hits = response.getHits();
        List<DocSearchSimpleVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            DocSearchSimpleVO docSearchSimpleVO = objectMapper.readValue(hit.getSourceAsString(), DocSearchSimpleVO.class);
            records.add(docSearchSimpleVO);
        }

        // 返回数据
        return new PageVO<DocSearchSimpleVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    private static final int DEFAULT_TERMS_SIZE = 1000;

    @Override
    public DynamicConditionVO getDynamicCondition(DynamicConditionQueryDTO pageQuery) throws Exception {
        // source builder
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .query(getQueryBuilder(pageQuery))
                .size(0);

        // 前置处理
        for (DynamicConditionHandler dynamicConditionHandler : dynamicConditionHandlers) {
            if (dynamicConditionHandler.ignore(pageQuery)) {
                continue;
            }
            dynamicConditionHandler.beforeSearch(searchSourceBuilder, pageQuery, false);
        }

        // search request
        SearchRequest searchRequest = new SearchRequest(getIndex())
                .source(searchSourceBuilder);
        // do search
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        DynamicConditionVO dynamicConditionVO = new DynamicConditionVO();

        // 后置处理
        for (DynamicConditionHandler dynamicConditionHandler : dynamicConditionHandlers) {
            if (dynamicConditionHandler.ignore(pageQuery)) {
                continue;
            }
            dynamicConditionHandler.afterSearch(searchResponse, dynamicConditionVO, false, pageQuery);
        }

        Optional<DynamicConditionHandler> first = dynamicConditionHandlers.stream()
                .filter(handler -> handler.getListenFiled().equals(pageQuery.getOnChangeField()))
                .findFirst();

        // 若修改的是当前条件，需要把条件置空，重新查询
        if (first.isPresent()) {
            DynamicConditionHandler dynamicConditionHandler = first.get();
            BeanInfo beanInfo = Introspector.getBeanInfo(pageQuery.getClass());
            PropertyDescriptor pd = new PropertyDescriptor(pageQuery.getOnChangeField(), pageQuery.getClass());
            pd.getWriteMethod().invoke(pageQuery, (Object) null);
            SearchSourceBuilder otherSearchSourceBuilder = SearchSourceBuilder.searchSource()
                    .query(getQueryBuilder(pageQuery))
                    .size(0);
            dynamicConditionHandler.beforeSearch(otherSearchSourceBuilder, pageQuery, true);
            // search request
            SearchRequest otherSearchRequest = new SearchRequest(getIndex())
                    .source(searchSourceBuilder);
            // do search
            SearchResponse otherSearchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            dynamicConditionHandler.afterSearch(otherSearchResponse, dynamicConditionVO, true, pageQuery);
        }

        return dynamicConditionVO;
    }

    private List<DynamicConditionHandler> dynamicConditionHandlers = Arrays.asList(new EnterpriseTypeConditionHandler(),
            new TaxPreferenceItemConditionHandler(),
            new ConditionParamHandler(),
            new TaxCategoriesCodeConditionHandler());

    /**
     * 动态参数处理器
     */
    interface DynamicConditionHandler {

        default String getListenFiled() {
            return "No listen field.";
        }

        default boolean ignore(DynamicConditionQueryDTO pageQuery) {
            return false;
        }

        String getTermsFiled();

        String getAggregationName();

        default void beforeSearch(SearchSourceBuilder searchSourceBuilder, DynamicConditionQueryDTO pageQuery, boolean onChange) {
            TermsAggregationBuilder aggregation = AggregationBuilders.terms(getAggregationName())
                    .field(getTermsFiled())
                    .size(DEFAULT_TERMS_SIZE);
            searchSourceBuilder.aggregation(aggregation);
        }

        default void afterSearch(SearchResponse searchResponse, DynamicConditionVO dynamicConditionVO, boolean onChange, DynamicConditionQueryDTO pageQuery) {
            List<String> keys = getTermsAggregationBucketKeys(searchResponse, getAggregationName());
            dynamicConditionVO.setEnterpriseTypes(keys);
        }
    }

    static class EnterpriseTypeConditionHandler implements DynamicConditionHandler {

        public String getListenFiled() {
            return "enterpriseTypes";
        }

        @Override
        public String getTermsFiled() {
            return "enterpriseType";
        }

        @Override
        public String getAggregationName() {
            return "enterpriseTypes";
        }
    }

    static class TaxPreferenceItemConditionHandler implements DynamicConditionHandler {

        @Override
        public String getListenFiled() {
            return "taxPreferenceItems";
        }

        @Override
        public String getTermsFiled() {
            return "taxPreferenceItem";
        }

        @Override
        public String getAggregationName() {
            return "taxPreferenceItems";
        }
    }

    static class ConditionParamHandler implements DynamicConditionHandler {

        private String nestedName = "nestedName";

        private String termsName = "termsName";

        @Override
        public void beforeSearch(SearchSourceBuilder searchSourceBuilder, DynamicConditionQueryDTO pageQuery, boolean onChange) {
            NestedAggregationBuilder aggregation = AggregationBuilders.nested(nestedName, "conditions")
                    .subAggregation(AggregationBuilders.terms(termsName).field("conditions.conditionName").size(DEFAULT_TERMS_SIZE));
            searchSourceBuilder.aggregation(aggregation);
        }

        @Override
        public void afterSearch(SearchResponse searchResponse, DynamicConditionVO dynamicConditionVO, boolean onChange, DynamicConditionQueryDTO pageQuery) {
            // nested terms 获取所有条件名称
            List<String> conditionNames = getNestedAggregationBucketKeys(searchResponse, nestedName, termsName);
            Set<String> conditionNameSet = new HashSet<>(conditionNames);
            // 数据映射
            SysParamService sysParamService = SpringUtil.getBean(SysParamService.class);
            List<GroupVO<DynamicConditionVO.Condition>> conditions = sysParamService.getSysParamDOByTypes(SysParamTypes.TAX_PREFERENCE_CONDITION).stream()
                    .filter(sysParamDO -> conditionNameSet.contains(sysParamDO.getParamName()))
                    .filter(sysParamDO -> {
                        // 增加业务规则，在没有选中减免事项的时候，仅展示 其他筛选条件
                        if (CollectionUtils.isEmpty(pageQuery.getTaxPreferenceItems())) {
                            return "其他筛选".equals(sysParamDO.getExtendsField2());
                        }
                        return true;
                    })
                    .sorted(Comparator.comparing(SysParamDO::getParamKey))
                    .collect(Collectors.groupingBy(SysParamDO::getExtendsField2, LinkedHashMap::new, Collectors.toList()))
                    .entrySet().stream()
                    .map(entry -> {
                        List<DynamicConditionVO.Condition> values = entry.getValue().stream()
                                .map(sysParamDO -> {
                                            DynamicConditionVO.Condition condition = new DynamicConditionVO.Condition();
                                            condition.setMultipleChoice("多选".equals(sysParamDO.getExtendsField4()))
                                                    .setName(sysParamDO.getParamName())
                                                    .setValues(CustomStringUtil.arrayStringToList(sysParamDO.getParamValue()));
                                            return condition;
                                        }
                                )
                                .collect(Collectors.toList());
                        return new GroupVO<DynamicConditionVO.Condition>()
                                .setName(entry.getKey())
                                .setValues(values);
                    }).collect(Collectors.toList());
            dynamicConditionVO.setConditions(conditions);
        }

        public boolean ignore(DynamicConditionQueryDTO pageQuery) {
            return !CollectionUtils.isEmpty(pageQuery.getTaxCategoriesCodes()) ||
                    !CollectionUtils.isEmpty(pageQuery.getTaxPreferenceItems());
        }

        @Override
        public String getTermsFiled() {
            return null;
        }

        @Override
        public String getAggregationName() {
            return null;
        }
    }

    static class TaxCategoriesCodeConditionHandler implements DynamicConditionHandler {

        @Override
        public String getTermsFiled() {
            return "taxCategories.codeValue";
        }

        @Override
        public String getAggregationName() {
            return "taxCategoriesCodes";
        }
    }

    /**
     * 获取terms aggregation key集合
     */
    private static List<String> getTermsAggregationBucketKeys(SearchResponse response, String name) {
        Aggregations aggregations = response.getAggregations();
        ParsedStringTerms parsedStringTerms = aggregations.get(name);
        return parsedStringTerms.getBuckets().stream()
                .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                .collect(Collectors.toList());
    }

    /**
     *  获取nested aggregation key集合
     */
    private static List<String> getNestedAggregationBucketKeys(SearchResponse searchResponse,String nestedName, String targetName) {
        ParsedNested nestedAggregation = searchResponse.getAggregations().get(nestedName);
        ParsedTerms termsAggregation = nestedAggregation.getAggregations().get(targetName);
        List<? extends Terms.Bucket> buckets = termsAggregation.getBuckets();
        return buckets.stream().map(MultiBucketsAggregation.Bucket::getKeyAsString).collect(Collectors.toList());
    }

    @Override
    public Class<TaxPreferenceSearchListVO> getResultClass() {
        return TaxPreferenceSearchListVO.class;
    }

    private String getIndex() {
        TaxPreferenceSearchQueryDTO queryDTO = new TaxPreferenceSearchQueryDTO();
        return queryDTO.index();
    }
}
