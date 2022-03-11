package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.*;
import cn.huacloud.taxpreference.common.utils.RedisKeyUtil;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import cn.huacloud.taxpreference.services.consumer.ConsultationSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ApproximateConsultationDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationContentESVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationCountVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationESVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * 热门咨询检索
 *
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class ConsultationSearchServiceImpl implements ConsultationSearchService {

    @Getter
    private final RestHighLevelClient restHighLevelClient;
    @Getter
    private final ObjectMapper objectMapper;

    private final ElasticsearchIndexConfig indexConfig;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public Class<ConsultationESVO> getResultClass() {
        return ConsultationESVO.class;
    }


    @Override
    public String[] getExcludeSource() {
        return new String[]{};
    }

    @Override
    public PageVO<ConsultationESVO> hotConsultation(PageQueryDTO pageQuery) throws Exception {
        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                matchQuery("published", 1),
                pageQuery,
                SortBuilders.fieldSort("views").order(SortOrder.DESC),
                SortBuilders.fieldSort("finishTime").order(SortOrder.DESC));

        // 数据映射
        SearchHits hits = response.getHits();
        List<ConsultationESVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            ConsultationESVO consultationESVO = objectMapper.readValue(hit.getSourceAsString(), ConsultationESVO.class);
            records.add(consultationESVO);
        }

        // 返回数据
        return new PageVO<ConsultationESVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    @Override
    public PageVO<ConsultationESVO> latestConsultation(PageQueryDTO pageQuery) throws Exception {

        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                matchQuery("published", 1),
                pageQuery,
                SortBuilders.fieldSort("finishTime").order(SortOrder.DESC));


        // 数据映射
        SearchHits hits = response.getHits();
        List<ConsultationESVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            ConsultationESVO consultationESVO = objectMapper.readValue(hit.getSourceAsString(), ConsultationESVO.class);
            records.add(consultationESVO);
        }

        // 返回数据
        return new PageVO<ConsultationESVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    @Override
    public ConsultationESVO getConsultationDetails(Long id) throws IOException {
        GetRequest request = new GetRequest(getIndex());
        request.id(id.toString());
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        if (!response.isExists()) {
            throw BizCode._4500.exception();
        }
        ConsultationESVO consultationESVO = objectMapper.readValue(response.getSourceAsString(), ConsultationESVO.class);
        List<ConsultationContentESVO> collect = consultationESVO.getConsultationContent()
                .stream()
                .filter(consultationContentDO -> consultationContentDO.getContentType().equals(ContentType.QUESTION))
                .collect(Collectors.toList());
        consultationESVO.setAppendCount(collect.size());
        return consultationESVO;
    }

    @Override
    public ConsultationCountVO getCount() throws Exception {
        //热门咨询总数
        ConsultationCountVO consultationCountVO = new ConsultationCountVO();
        //获取问题个数
        long questionCount = docCountQuery(DocType.CONSULTATION, matchQuery("published", 1));
        //获取企业个数
        long customCount = getCustomCount();
        consultationCountVO.setQuestionTotals(questionCount);
        consultationCountVO.setCustomTotals(customCount);
        return consultationCountVO;
    }

    @Override
    public PageVO<ConsultationESVO> approximateConsultation(ApproximateConsultationDTO pageQuery) throws Exception {
        // 执行查询
        QueryBuilder queryBuilder;
        if (pageQuery.getIndustryCodes() == null || CollectionUtils.isEmpty(pageQuery.getIndustryCodes())) {
            queryBuilder = matchAllQuery();
        } else {
            queryBuilder = matchQuery("industries.codeValue", pageQuery.getIndustryCodes().get(0));
        }
        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                queryBuilder,
                pageQuery,
                SortBuilders.fieldSort("views").order(SortOrder.DESC)
        );

        // 数据映射
        SearchHits hits = response.getHits();
        List<ConsultationESVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            ConsultationESVO consultationESVO = objectMapper.readValue(hit.getSourceAsString(), ConsultationESVO.class);
            if (consultationESVO.getId().equals(pageQuery.getId())) {
                continue;
            }
            records.add(consultationESVO);
        }
        if (records.size() > 4) {
            records = records.subList(0, 4);
        }
        // 返回数据
        return new PageVO<ConsultationESVO>()
                .setTotal((long) records.size())
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    @Override
    public PageVO<ConsultationESVO> myConsultation(PageQueryDTO pageQuery, Long currentUserId) throws Exception {

        //点击时查看是否存在已回复的消息
        String readStatus = stringRedisTemplate.opsForValue().get(RedisKeyUtil.getConsultationReplyRedisKey(currentUserId));
        if (RedPointStatus.SHOW.getValue().equals(readStatus)) {
            stringRedisTemplate.delete(RedisKeyUtil.getConsultationReplyRedisKey(currentUserId));
        }

        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                matchQuery("customerUserId", currentUserId),
                pageQuery,
                SortBuilders.fieldSort("views").order(SortOrder.DESC),
                SortBuilders.fieldSort("finishTime").order(SortOrder.DESC));

        // 数据映射
        SearchHits hits = response.getHits();
        List<ConsultationESVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            ConsultationESVO consultationESVO = objectMapper.readValue(hit.getSourceAsString(), ConsultationESVO.class);
            records.add(consultationESVO);
        }

        // 返回数据
        return new PageVO<ConsultationESVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    @Override
    public RedPointStatus replyTipsConsultation(Long currentUserId) {

        String status = stringRedisTemplate.opsForValue().get(RedisKeyUtil.getConsultationReplyRedisKey(currentUserId));
        if (status == null) {
            //代表没有提问
            return RedPointStatus.HIDDEN;
        } else {
        //    stringRedisTemplate.delete(RedisKeyUtil.getConsultationReplyRedisKey(currentUserId));
            return RedPointStatus.valueOf(status);
        }
    }

    private long getCustomCount() throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        AggregationBuilder aggregation = AggregationBuilders.cardinality("DISTINCT_TOTAL_COUNT").field("customerUserId");
        searchSourceBuilder.aggregation(aggregation);
        SearchRequest request = new SearchRequest(DocType.CONSULTATION.indexGetter.apply(indexConfig).getAlias());
        request.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Cardinality cardinality = aggregations.get("DISTINCT_TOTAL_COUNT");
        long customCount = cardinality.getValue();
        return customCount;
    }

    private String getIndex() {
        ConsultationQueryDTO queryDTO = new ConsultationQueryDTO();
        return queryDTO.index();
    }

    public Long docCountQuery(DocType docType, QueryBuilder queryBuilder) throws Exception {
        CountRequest request = new CountRequest(docType.indexGetter.apply(indexConfig).getAlias())
                .query(queryBuilder);
        CountResponse countResponse = restHighLevelClient.count(request, RequestOptions.DEFAULT);
        return countResponse.getCount();
    }
}
