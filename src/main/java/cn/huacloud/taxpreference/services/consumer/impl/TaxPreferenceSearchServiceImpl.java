package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.consumer.TaxPreferenceSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.TaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotLabelVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PreviousNextVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.TaxPreferenceSearchVO;
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
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

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
        BoolQueryBuilder queryBuilder = generatorDefaultQueryBuilder(pageQuery);

        // 是否使用推荐
        if (pageQuery.getUseRecommend()) {
            // TODO 设置智能推荐的匹配过滤条件
        }

        // 关键字查询
        String keyword = pageQuery.getKeyword();
        if (keyword != null) {
            List<String> searchFields = pageQuery.searchFields();
            for (String searchField : searchFields) {
                queryBuilder.should(matchPhraseQuery(searchField, keyword));
            }
        }

        // 标签过滤
        String label = pageQuery.getLabel();
        if (label != null) {
            queryBuilder.must(wildcardQuery("labels", formatWildcardValue(label)));
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
    public Class<TaxPreferenceSearchVO> getResultClass() {
        return TaxPreferenceSearchVO.class;
    }

    private String getIndex() {
        TaxPreferenceSearchQueryDTO queryDTO = new TaxPreferenceSearchQueryDTO();
        return queryDTO.index();
    }
}
