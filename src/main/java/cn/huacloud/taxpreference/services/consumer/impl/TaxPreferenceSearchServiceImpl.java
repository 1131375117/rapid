package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.GroupVO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.common.utils.CustomStringUtil;
import cn.huacloud.taxpreference.services.common.SysParamService;
import cn.huacloud.taxpreference.services.common.entity.dos.SysParamDO;
import cn.huacloud.taxpreference.services.consumer.TaxPreferenceSearchService;
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
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
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

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Function;
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
    public DynamicConditionVO getDynamicCondition(TaxPreferenceSearchQueryDTO pageQuery) throws Exception {
        // 动态条件视图
        DynamicConditionVO dynamicConditionVO = new DynamicConditionVO();

        // 企业类型
        {
            TermsAggregationHandler handler = new TermsAggregationHandler("enterpriseTypes", "enterpriseType", "enterpriseTypes");
            SearchResponse searchResponse = handler.doSearch(pageQuery, this::getQueryBuilder, restHighLevelClient, getIndex());
            handler.setProperty(searchResponse, dynamicConditionVO);
        }

        // 减免事项
        {
            TermsAggregationHandler handler = new TermsAggregationHandler("taxPreferenceItems", "taxPreferenceItem", "taxPreferenceItems");
            SearchResponse searchResponse = handler.doSearch(pageQuery, this::getQueryBuilder, restHighLevelClient, getIndex());
            handler.setProperty(searchResponse, dynamicConditionVO);
        }

        // 动态条件
        if (!CollectionUtils.isEmpty(pageQuery.getTaxCategoriesCodes()) || !CollectionUtils.isEmpty(pageQuery.getTaxPreferenceItems())) {
            TermsAggregationHandler handler = new TermsAggregationHandler("conditions", "conditions.conditionName", "conditions") {

                private String subAggregationName = "subAggregation";

                @Override
                public AggregationBuilder getAggregationBuilder() {
                    return AggregationBuilders.nested(getAggregationName(), "conditions")
                            .subAggregation(AggregationBuilders.terms(subAggregationName).field(getField()).size(DEFAULT_TERMS_SIZE));
                }

                @Override
                public void setProperty(SearchResponse searchResponse, DynamicConditionVO dynamicConditionVO) throws Exception {
                    // nested terms 获取所有条件名称
                    List<String> conditionNames = getNestedAggregationBucketKeys(searchResponse, getAggregationName(), subAggregationName);
                    Set<String> conditionNameSet = new HashSet<>(conditionNames);
                    // 数据映射
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
            };
            SearchResponse searchResponse = handler.doSearch(pageQuery, this::getQueryBuilder, restHighLevelClient, getIndex());
            handler.setProperty(searchResponse, dynamicConditionVO);
        }
        // 视图美化
        dynamicConditionVO.viewPretty();
        return dynamicConditionVO;
    }

    @Override
    public List<String> getItemRelatedCodes(List<String> taxPreferenceItems) throws Exception {
        if (CollectionUtils.isEmpty(taxPreferenceItems)) {
            return new ArrayList<>();
        }
        String itemRelatedCodes = "itemRelatedCodes";
        SearchRequest searchRequest = new SearchRequest(getIndex())
                .source(SearchSourceBuilder.searchSource()
                        .query(termsQuery("taxPreferenceItem", taxPreferenceItems))
                        .size(0)
                        .aggregation(AggregationBuilders.terms(itemRelatedCodes)
                                .size(DEFAULT_TERMS_SIZE)
                                .field("taxCategories.codeValue")));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        return getTermsAggregationBucketKeys(searchResponse, itemRelatedCodes);
    }


    @Getter
    static class TermsAggregationHandler {
        private String aggregationName;
        private String field;
        private String targetProperty;
        private PropertyDescriptor propertyDescriptor;

        public TermsAggregationHandler(String aggregationName, String field, String targetProperty) {
            this.aggregationName = aggregationName;
            this.field = field;
            this.targetProperty = targetProperty;
            try {
                this.propertyDescriptor = new PropertyDescriptor(targetProperty, DynamicConditionVO.class);
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
        }

        public AggregationBuilder getAggregationBuilder() {
            return AggregationBuilders.terms(aggregationName)
                    .field(field)
                    .size(DEFAULT_TERMS_SIZE);
        }

        public SearchResponse doSearch(TaxPreferenceSearchQueryDTO pageQuery, Function<TaxPreferenceSearchQueryDTO, QueryBuilder> function, RestHighLevelClient restHighLevelClient, String index) throws Exception {
            TaxPreferenceSearchQueryDTO query = CustomBeanUtil.copyProperties(pageQuery, TaxPreferenceSearchQueryDTO.class);
            PropertyDescriptor pd = new PropertyDescriptor(targetProperty, TaxPreferenceSearchQueryDTO.class);
            pd.getWriteMethod().invoke(query, (Object) null);
            SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                    .query(function.apply(query))
                    .aggregation(getAggregationBuilder())
                    .size(0);
            SearchRequest searchRequest = new SearchRequest(index)
                    .source(searchSourceBuilder);
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        }

        public void setProperty(SearchResponse searchResponse, DynamicConditionVO dynamicConditionVO) throws Exception {
            List<String> property = getTermsAggregationBucketKeys(searchResponse, aggregationName);
            propertyDescriptor.getWriteMethod().invoke(dynamicConditionVO, property);
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
     * 获取nested aggregation key集合
     */
    private static List<String> getNestedAggregationBucketKeys(SearchResponse searchResponse, String nestedName, String targetName) {
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
