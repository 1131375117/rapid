package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.consumer.FrequentlyAskedQuestionSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.FAQSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PreviousNextVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class FrequentlyAskedQuestionSearchServiceImpl implements FrequentlyAskedQuestionSearchService {

    @Getter
    private final RestHighLevelClient restHighLevelClient;
    @Getter
    private final ObjectMapper objectMapper;

    @Override
    public Class<FAQSearchVO> getResultClass() {
        return FAQSearchVO.class;
    }

    @Override
    public PageVO<FAQSearchSimpleVO> hotFAQList(PageQueryDTO pageQuery) throws Exception {
        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(), matchAllQuery(), pageQuery, SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC));

        // 数据映射
        SearchHits hits = response.getHits();
        List<FAQSearchSimpleVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            FAQSearchSimpleVO faqSearchSimpleVO = objectMapper.readValue(hit.getSourceAsString(), FAQSearchSimpleVO.class);
            records.add(faqSearchSimpleVO);
        }

        // 返回数据
        return new PageVO<FAQSearchSimpleVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    @Override
    public FAQSearchVO getFAQDetails(Long id) throws Exception {
        GetRequest request = new GetRequest(getIndex());
        request.id(id.toString());
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        if (!response.isExists()) {
            throw BizCode._4500.exception();
        }
        FAQSearchVO faqSearchVO = objectMapper.readValue(response.getSourceAsString(), FAQSearchVO.class);
        // 设置上一篇、下一篇
        PreviousNextVO<Long> defaultPreviousNext = getDefaultPreviousNext(getIndex(), id);
        faqSearchVO.setPreviousNext(defaultPreviousNext);
        return faqSearchVO;
    }

    @Override
    public PageVO<FAQSearchVO> policiesRelatedFAQ(Long policiesId, PageQueryDTO pageQuery) throws Exception {
        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                termsQuery("policiesIds", Collections.singletonList(policiesId)),
                pageQuery);
        // 数据映射
        SearchHits hits = response.getHits();

        List<FAQSearchVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            FAQSearchVO faqSearchVO = objectMapper.readValue(hit.getSourceAsString(), FAQSearchVO.class);
            records.add(faqSearchVO);
        }

        // 返回数据
        return new PageVO<FAQSearchVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    @Override
    public List<String> getFaqDocSource(Integer size) throws Exception {
        // name
        String aggregationName = "docSourceTerms";
        // search request
        SearchRequest searchRequest = new SearchRequest(getIndex())
                .source(SearchSourceBuilder.searchSource()
                        .size(0)
                        .aggregation(AggregationBuilders.terms(aggregationName)
                                .field("docSource")
                                .size(size)));
        // do search
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // get terms
        ParsedTerms parsedTerms = searchResponse.getAggregations().get(aggregationName);
        // mapping return
        return parsedTerms.getBuckets().stream()
                .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                .collect(Collectors.toList());
    }

    /**
     * 获取ES索引名称（其实是别名）
     *
     * @return ES索引名称
     */
    private String getIndex() {
        FAQSearchQueryDTO queryDTO = new FAQSearchQueryDTO();
        return queryDTO.index();
    }
}
