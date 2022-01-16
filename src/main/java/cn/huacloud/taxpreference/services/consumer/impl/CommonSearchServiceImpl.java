package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.ExSearchQueryDTO;
import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.consumer.SearchScope;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeCountVO;
import cn.huacloud.taxpreference.services.consumer.*;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.*;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotContentVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.SearchDataCountVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class CommonSearchServiceImpl implements CommonSearchService {

    private final RestHighLevelClient restHighLevelClient;

    private final ElasticsearchIndexConfig indexConfig;

    private final ObjectMapper objectMapper;

    private final PoliciesSearchService policiesSearchService;

    private final PoliciesExplainSearchService policiesExplainSearchService;

    private final FrequentlyAskedQuestionSearchService frequentlyAskedQuestionSearchService;

    private final TaxPreferenceSearchService taxPreferenceSearchService;

    private final OtherDocSearchService otherDocSearchService;

    private static final DocType[] ALL_DOC_TYPES = {DocType.POLICIES,
            DocType.POLICIES_EXPLAIN,
            DocType.FREQUENTLY_ASKED_QUESTION,
            DocType.TAX_PREFERENCE,
            DocType.CASE_ANALYSIS};

    private static final String[] FETCH_SOURCE = {"id", "docType.codeName", "docType.codeValue", "title", "releaseDate", "labels"};

    @Override
    public PageVO<HotContentVO> weeklyHotContent(PageQueryDTO pageQuery) throws Exception {

        // static param
        String[] indices = getIndices(ALL_DOC_TYPES);

        // query builder
        BoolQueryBuilder queryBuilder = boolQuery().should(matchQuery("docType.codeValue", DocType.CASE_ANALYSIS.name()))
                .should(boolQuery().mustNot(existsQuery("docType.codeValue")));

        // source builder
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .trackTotalHits(true)
                .query(queryBuilder)
                .fetchSource(FETCH_SOURCE, null)
                .from(pageQuery.from())
                .size(pageQuery.getPageSize())
                .sort("releaseDate", SortOrder.DESC);

        // request builder
        SearchRequest request = new SearchRequest(indices)
                .source(searchSourceBuilder);

        // do search
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        // mapping data
        SearchHits hits = response.getHits();

        List<HotContentVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            String index = hit.getIndex();
            HotContentVO hotContentVO = objectMapper.readValue(hit.getSourceAsString(), HotContentVO.class);
            setDocType(index, hotContentVO);
            records.add(hotContentVO);
        }

        return new PageVO<HotContentVO>()
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setTotal(hits.getTotalHits().value)
                .setRecords(records);
    }

    @Override
    public PageVO<HotContentVO> guessYouLike(GuessYouLikeQueryDTO pageQuery) throws Exception {
        DocType docType = pageQuery.getDocType();
        String[] indices;
        if (docType == null) {
            // query all type
            indices = getIndices(ALL_DOC_TYPES);
        } else {
            // query single type
            indices = new String[]{docType.indexGetter.apply(indexConfig).getAlias()};
        }

        // query builder
        BoolQueryBuilder queryBuilder = boolQuery().should(matchQuery("docType.codeValue", DocType.CASE_ANALYSIS.name()))
                .should(boolQuery().mustNot(existsQuery("docType.codeValue")));

        // source builder
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .trackTotalHits(true)
                .query(queryBuilder)
                .fetchSource(FETCH_SOURCE, null)
                .from(pageQuery.from())
                .size(pageQuery.getPageSize())
                .sort("releaseDate", SortOrder.DESC);

        // request builder
        SearchRequest request = new SearchRequest(indices)
                .source(searchSourceBuilder);

        // do search
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        // mapping data
        SearchHits hits = response.getHits();

        List<HotContentVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            String index = hit.getIndex();
            HotContentVO hotContentVO = objectMapper.readValue(hit.getSourceAsString(), HotContentVO.class);
            setDocType(index, hotContentVO);
            records.add(hotContentVO);
        }

        return new PageVO<HotContentVO>()
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setTotal(hits.getTotalHits().value)
                .setRecords(records);
    }

    @Override
    public List<SysCodeCountVO> allDocCount(ExSearchQueryDTO pageQuery) throws Exception {

        List<SysCodeCountVO> sysCodeCountVOList = new ArrayList<>();
        // 政策法规
        {
            PoliciesSearchQueryDTO queryDTO = new PoliciesSearchQueryDTO();
            BeanUtils.copyProperties(pageQuery, queryDTO);
            queryDTO.paramReasonable();
            QueryBuilder queryBuilder = policiesSearchService.getQueryBuilder(queryDTO);
            SysCodeCountVO sysCodeCountVO = getDeclareDocTypeCount(DocType.POLICIES, queryBuilder);
            sysCodeCountVOList.add(sysCodeCountVO);
        }

        // 政策解读
        {
            PoliciesExplainSearchQueryDTO queryDTO = new PoliciesExplainSearchQueryDTO();
            BeanUtils.copyProperties(pageQuery, queryDTO);
            queryDTO.paramReasonable();
            QueryBuilder queryBuilder = policiesExplainSearchService.getQueryBuilder(queryDTO);
            SysCodeCountVO sysCodeCountVO = getDeclareDocTypeCount(DocType.POLICIES_EXPLAIN, queryBuilder);
            sysCodeCountVOList.add(sysCodeCountVO);
        }

        // 热门问答
        {
            FAQSearchQueryDTO queryDTO = new FAQSearchQueryDTO();
            BeanUtils.copyProperties(pageQuery, queryDTO);
            queryDTO.paramReasonable();
            QueryBuilder queryBuilder = frequentlyAskedQuestionSearchService.getQueryBuilder(queryDTO);
            SysCodeCountVO sysCodeCountVO = getDeclareDocTypeCount(DocType.FREQUENTLY_ASKED_QUESTION, queryBuilder);
            sysCodeCountVOList.add(sysCodeCountVO);
        }

        // 税收优惠
        {
            TaxPreferenceSearchQueryDTO queryDTO = new TaxPreferenceSearchQueryDTO();
            BeanUtils.copyProperties(pageQuery, queryDTO);
            queryDTO.setSearchScope(SearchScope.TITLE_AND_CONTENT);
            queryDTO.paramReasonable();
            QueryBuilder queryBuilder = taxPreferenceSearchService.getQueryBuilder(queryDTO);
            SysCodeCountVO sysCodeCountVO = getDeclareDocTypeCount(DocType.TAX_PREFERENCE, queryBuilder);
            sysCodeCountVOList.add(sysCodeCountVO);
        }

        // 案例分析
        {
            OtherDocQueryDTO queryDTO = new OtherDocQueryDTO();
            BeanUtils.copyProperties(pageQuery, queryDTO);
            queryDTO.setDocType(DocType.CASE_ANALYSIS);
            queryDTO.paramReasonable();
            QueryBuilder queryBuilder = otherDocSearchService.getQueryBuilder(queryDTO);
            SysCodeCountVO sysCodeCountVO = getDeclareDocTypeCount(DocType.CASE_ANALYSIS, queryBuilder);
            sysCodeCountVOList.add(sysCodeCountVO);
        }

        return sysCodeCountVOList;
    }

    @Override
    public SearchDataCountVO getDataCount() throws Exception {
        SearchDataCountVO searchDataCountVO = new SearchDataCountVO();
        // 中央政策总数
        Long central = docCountQuery(DocType.POLICIES, matchQuery("area.codeValue", "中央"));
        searchDataCountVO.setCentralPoliciesCount(central);
        // 地方政策总数
        Long local = docCountQuery(DocType.POLICIES, boolQuery().mustNot(matchQuery("area.codeValue", "中央")));
        searchDataCountVO.setLocalPoliciesCount(local);
        // 税收优惠总数
        Long taxPreference = docCountQuery(DocType.TAX_PREFERENCE, matchAllQuery());
        searchDataCountVO.setTaxPreferenceCount(taxPreference);
        // 官方问答总数
        Long faq = docCountQuery(DocType.FREQUENTLY_ASKED_QUESTION, matchAllQuery());
        searchDataCountVO.setFaqCont(faq);

        return searchDataCountVO;
    }

    /**
     * 查询文档数
     * @param docType 文档类型
     * @param queryBuilder 查询构建器
     * @return 统计数
     */
    private Long docCountQuery(DocType docType, QueryBuilder queryBuilder) throws Exception {
        CountRequest request = new CountRequest(docType.indexGetter.apply(indexConfig).getAlias())
                .query(queryBuilder);
        CountResponse countResponse = restHighLevelClient.count(request, RequestOptions.DEFAULT);
        return countResponse.getCount();
    }

    /**
     * 查询具体类型的数据文档数据
     * @param docType 文档类型
     * @param queryBuilder 查询构建器
     * @return 数据统计视图
     */
    private SysCodeCountVO getDeclareDocTypeCount(DocType docType, QueryBuilder queryBuilder) throws IOException {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .trackTotalHits(true)
                .query(queryBuilder)
                .size(0);
        SearchRequest searchRequest = new SearchRequest(docType.indexGetter.apply(indexConfig).getAlias())
                .source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        TotalHits totalHits = response.getHits().getTotalHits();
        return SysCodeCountVO.of(docType.getSysCode())
                .setTotal(totalHits.value);
    }

    /**
     * 获取查询的索引
     *
     * @param docTypes 文档类型数组
     * @return 索引别名数组
     */
    private String[] getIndices(DocType[] docTypes) {
        return Arrays.stream(docTypes)
                .map(docType -> docType.indexGetter.apply(indexConfig).getAlias())
                .distinct()
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }

    /**
     * 设置文档类型
     *
     * @param index        索引名称
     * @param hotContentVO 热点内容
     */
    private void setDocType(String index, HotContentVO hotContentVO) {
        if (hotContentVO.getHotContentType() != null) {
            return;
        }
        DocType docType = DocType.getDocTypeByIndex(index);
        hotContentVO.setHotContentType(docType.getSysCode());
    }
}
