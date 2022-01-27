package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.consumer.FrequentlyAskedQuestionSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.FAQSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.FAQSearchVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PreviousNextVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.OrganizationVO;
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
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        SearchResponse response = simplePageSearch(getIndex(), matchAllQuery(), pageQuery,SortBuilders.fieldSort("views").order(SortOrder.DESC), SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC));

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
    public List<OrganizationVO> getFaqAnswerOrganization(Integer size) throws Exception {
        // name
        String aggregationName = "answerOrganizationTerms";
        String aggregationtype = "organizationTypeTerms";
        // search request
        SearchRequest searchRequest = new SearchRequest(getIndex())
                .source(SearchSourceBuilder.searchSource()
                        .size(0)
                        .aggregation(AggregationBuilders.terms(aggregationtype)
                                .field("organizationType")
                                .subAggregation(AggregationBuilders.terms(aggregationName).field("answerOrganization").size(size))
                                .size(size)
                        ));
        // do search
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // get terms
        ParsedTerms parsedTerms = searchResponse.getAggregations().get(aggregationtype);
        // mapping return
        List<OrganizationVO> headList = new ArrayList<>();
        OrganizationVO head = new OrganizationVO();
        head.setTitle("解答机构");
        head.setValue("");
        List<OrganizationVO> organizationVOS = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = parsedTerms.getBuckets();
        Collections.reverse(buckets);
        for (Terms.Bucket bucket : buckets) {
            OrganizationVO organizationVO = new OrganizationVO();
            ParsedTerms parsedTerms1 = bucket.getAggregations().get(aggregationName);
            List<String> nameList = parsedTerms1.getBuckets().stream().map(MultiBucketsAggregation.Bucket::getKeyAsString)
                    .collect(Collectors.toList());
            ArrayList<OrganizationVO> childList = new ArrayList<>();
            for (String name : nameList) {
                OrganizationVO organizationVO1 = new OrganizationVO();
                organizationVO1.setTitle(name);
                organizationVO1.setValue(name);
                childList.add(organizationVO1);
            }

            organizationVO.setChildren(childList);
            organizationVO.setTitle((String) bucket.getKey());
            organizationVO.setValue((String) bucket.getKey());
            organizationVO.setOrganizationType(String.valueOf(bucket.getKey()));
            if("专业机构".equals(bucket.getKey())){
                organizationVO.setChildren(null);
            }
            organizationVOS.add(organizationVO);
        }
        head.setChildren(organizationVOS);
        headList.add(head);
        return headList;
    }

    @Override
    public List<String> getFaqSubjectType(Integer size) throws IOException {
        String aggregationName = "subjectType";
        SearchRequest searchRequest = new SearchRequest(getIndex())
                .source(SearchSourceBuilder.searchSource()
                        .size(0)
                        .aggregation(AggregationBuilders.terms(aggregationName)
                                .field("subjectType")
                                .size(size)
                        ));
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
