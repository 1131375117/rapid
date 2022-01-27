package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.consumer.OtherDocSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.DocSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

/**
 * 案例分析实现类
 *
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class OtherDocSearchServiceImpl implements OtherDocSearchService {
    @Getter
    private final RestHighLevelClient restHighLevelClient;
    @Getter
    private final ObjectMapper objectMapper;

    @Override
    public Class<OtherDocVO> getResultClass() {
        return OtherDocVO.class;
    }

    @Override
    public String[] getExcludeSource() {
        return new String[]{"htmlContent"};
    }

    @Override
    public OtherDocVO getTaxOtherDocDetails(Long id) throws Exception {
        GetRequest request = new GetRequest(getIndex());
        request.id(id.toString());
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        if (!response.isExists()) {
            throw BizCode._4500.exception();
        }
        OtherDocVO otherDocVO = objectMapper.readValue(response.getSourceAsString(), OtherDocVO.class);
        // 设置上一篇、下一篇
        PreviousNextVO<Long> defaultPreviousNext = getDefaultPreviousNext(getIndex(), id);
        otherDocVO.setPreviousNext(defaultPreviousNext);
        return otherDocVO;
    }

    @Override
    public PageVO<DocSearchSimpleVO> latestCaseAnalyse(PageQueryDTO pageQuery) throws Exception {
        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                matchAllQuery(),
                pageQuery,
                SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC),
                SortBuilders.fieldSort("id").order(SortOrder.DESC));

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
    public List<String> getCaseAnalyseType(Integer size) throws IOException {

        String aggregationName="extendsField1Terms";
        SearchRequest searchRequest = new SearchRequest(getIndex())
                .source(SearchSourceBuilder.searchSource()
                        .size(0)
                        .aggregation(AggregationBuilders.terms(aggregationName)
                                .field("extendsField1")
                                .size(size)
                        ));
        // do search
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        // get terms
        ParsedTerms parsedTerms = searchResponse.getAggregations().get(aggregationName);

        return parsedTerms.getBuckets().stream()
                .map(MultiBucketsAggregation.Bucket::getKeyAsString)
                .collect(Collectors.toList());
    }

    private String getIndex() {
        OtherDocQueryDTO queryDTO = new OtherDocQueryDTO();
        return queryDTO.index();
    }
}
