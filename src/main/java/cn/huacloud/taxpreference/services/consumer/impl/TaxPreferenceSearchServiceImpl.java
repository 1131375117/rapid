package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.constants.SysParamTypes;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.GroupVO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
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
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

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
    public DynamicConditionVO getDynamicCondition(TaxPreferenceSearchQueryDTO pageQuery) throws IOException {
        QueryBuilder queryBuilder = getQueryBuilder(pageQuery);

        String enterpriseTypeTermsName = "enterpriseType";
        String taxPreferenceItemTermsName = "taxPreferenceItem";
        String conditionsTermsName = "conditions";

        boolean needConditions = !CollectionUtils.isEmpty(pageQuery.getTaxCategoriesCodes());

        // term aggregation
        // 企业类型
        TermsAggregationBuilder enterpriseTypeTerms = AggregationBuilders.terms(enterpriseTypeTermsName)
                .field("enterpriseType")
                .size(DEFAULT_TERMS_SIZE);
        // 减免事项
        TermsAggregationBuilder taxPreferenceItemTerms = AggregationBuilders.terms(taxPreferenceItemTermsName)
                .field("taxPreferenceItem")
                .size(DEFAULT_TERMS_SIZE);


        // source builder
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .query(queryBuilder)
                .aggregation(enterpriseTypeTerms)
                .aggregation(taxPreferenceItemTerms)
                .size(0);

        if (needConditions) {
            // 优惠条件
            TermsAggregationBuilder conditionsTerms = AggregationBuilders.terms(conditionsTermsName)
                    .field("conditions")
                    .size(DEFAULT_TERMS_SIZE);
            searchSourceBuilder.aggregation(conditionsTerms);
        }

        // search request
        SearchRequest searchRequest = new SearchRequest(getIndex())
                .source(searchSourceBuilder);
        // do search
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // data mapping
        List<String> enterpriseTypes = getAggregationBucketKeys(response, enterpriseTypeTermsName);

        List<String> taxPreferenceItems = getAggregationBucketKeys(response, taxPreferenceItemTermsName);

        DynamicConditionVO dynamicConditionVO = new DynamicConditionVO()
                .setEnterpriseTypes(enterpriseTypes)
                .setTaxPreferenceItems(taxPreferenceItems);

        List<String> conditionNames = getAggregationBucketKeys(response, conditionsTermsName);
        Set<String> conditionNameSet = new HashSet<>(conditionNames);
        if (needConditions) {
            List<GroupVO<GroupVO<String>>> conditions = sysParamService.getSysParamDOByTypes(SysParamTypes.TAX_PREFERENCE_CONDITION).stream()
                    .filter(sysParamDO -> conditionNameSet.contains(sysParamDO.getParamName()))
                    .sorted(Comparator.comparing(SysParamDO::getParamKey))
                    .collect(Collectors.groupingBy(SysParamDO::getExtendsField2, LinkedHashMap::new, Collectors.toList()))
                    .entrySet().stream()
                    .map(entry -> {
                        List<GroupVO<String>> values = entry.getValue().stream()
                                .map(sysParamDO -> new GroupVO<String>().setName(sysParamDO.getParamName())
                                        .setValues(CustomStringUtil.arrayStringToList(sysParamDO.getParamValue())))
                                .collect(Collectors.toList());
                        return new GroupVO<GroupVO<String>>()
                                .setName(entry.getKey())
                                .setValues(values);
                    }).collect(Collectors.toList());
            dynamicConditionVO.setConditions(conditions);
        }

        return dynamicConditionVO;
    }

    private List<String> getAggregationBucketKeys(SearchResponse response, String name) {
        Aggregations aggregations = response.getAggregations();
        ParsedStringTerms parsedStringTerms = aggregations.get(name);
        return parsedStringTerms.getBuckets().stream()
                .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                .collect(Collectors.toList());
    }

    @Override
    public Class<TaxPreferenceSearchVO> getResultClass() {
        return TaxPreferenceSearchVO.class;
    }

    private String getIndex() {
        TaxPreferenceSearchQueryDTO queryDTO = new TaxPreferenceSearchQueryDTO();
        return queryDTO.index();
    }
}
