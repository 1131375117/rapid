package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.consumer.PoliciesExplainSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesExplainSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.ess.PoliciesExplainES;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchListVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchVO;
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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class PoliciesExplainSearchServiceImpl implements PoliciesExplainSearchService {

    @Getter
    private final RestHighLevelClient restHighLevelClient;
    @Getter
    private final ObjectMapper objectMapper;

    @Override
    public Class<PoliciesExplainSearchListVO> getResultClass() {
        return PoliciesExplainSearchListVO.class;
    }

    @Override
    public PoliciesExplainSearchVO getPoliciesExplainDetails(String id) throws Exception {
        GetRequest request = new GetRequest(getIndex());
        request.id(id);
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        if (response.isExists()) {
            throw BizCode._4500.exception();
        }

        PoliciesExplainSearchVO policiesExplainSearchVO = new PoliciesExplainSearchVO();
        PoliciesExplainES policiesExplainES = objectMapper.readValue(response.getSourceAsString(), PoliciesExplainES.class);
        BeanUtils.copyProperties(policiesExplainES, policiesExplainSearchVO);
        return policiesExplainSearchVO;
    }

    @Override
    public PageVO<PoliciesExplainSearchSimpleVO> latestPoliciesExplain(PageQueryDTO pageQuery) throws Exception {
        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                matchAllQuery(),
                pageQuery,
                SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC));

        // 数据映射
        SearchHits hits = response.getHits();
        List<PoliciesExplainSearchSimpleVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            PoliciesExplainSearchSimpleVO policiesSearchSimpleVO = objectMapper.readValue(hit.getSourceAsString(), PoliciesExplainSearchSimpleVO.class);
            records.add(policiesSearchSimpleVO);
        }

        // 返回数据
        return new PageVO<PoliciesExplainSearchSimpleVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    @Override
    public PoliciesExplainSearchSimpleVO policiesRelatedExplain(String policiesId) throws Exception {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .trackTotalHits(true)
                .query(matchAllQuery())
                .from(0)
                .size(1)
                .sort("releaseDate", SortOrder.DESC);

        SearchRequest request = new SearchRequest(getIndex());
        request.source(searchSourceBuilder);

        // 执行查询
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        // 数据映射
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits().value;

        // 没有找到对应解读
        if (total < 1) {
            return null;
        }

        SearchHit hit = hits.getHits()[0];
        return objectMapper.readValue(hit.getSourceAsString(), PoliciesExplainSearchSimpleVO.class);
    }

    /**
     * 获取ES索引名称（其实是别名）
     *
     * @return ES索引名称
     */
    private String getIndex() {
        PoliciesExplainSearchQueryDTO queryDTO = new PoliciesExplainSearchQueryDTO();
        return queryDTO.indices()[0];
    }
}
