package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.consumer.PoliciesSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.ess.PoliciesES;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchListVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PreviousNextVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class PoliciesSearchServiceImpl implements PoliciesSearchService {

    @Getter
    private final RestHighLevelClient restHighLevelClient;
    @Getter
    private final ObjectMapper objectMapper;

    private final SysCodeService sysCodeService;

    @Override
    public QueryBuilder getQueryBuilder(PoliciesSearchQueryDTO pageQuery) {
        BoolQueryBuilder queryBuilder = generatorDefaultQueryBuilder(pageQuery);

        // 关键字查询
        String keyword = pageQuery.getKeyword();
        if (keyword != null) {
            List<String> searchFields = pageQuery.searchFields();
            for (String searchField : searchFields) {
                queryBuilder.should(matchPhraseQuery(searchField, keyword));
            }
        }

        // 文号查询
        String docCode = pageQuery.getDocCode();
        if (docCode != null) {
            queryBuilder.must(wildcardQuery("docCode", formatWildcardValue(docCode)));
        }

        return queryBuilder;
    }

    @Override
    public Class<PoliciesSearchListVO> getResultClass() {
        return PoliciesSearchListVO.class;
    }

    @Override
    public PoliciesSearchVO getPoliciesDetails(Long id) throws Exception {
        GetRequest request = new GetRequest(getIndex());
        request.id(id.toString());
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        if (!response.isExists()) {
            throw BizCode._4500.exception();
        }

        PoliciesES policiesES = objectMapper.readValue(response.getSourceAsString(), PoliciesES.class);
        PoliciesSearchVO policiesSearchVO = CustomBeanUtil.copyProperties(policiesES, PoliciesSearchVO.class);
        // 设置上一篇、下一篇
        PreviousNextVO<Long> defaultPreviousNext = getDefaultPreviousNext(getIndex(), id);
        policiesSearchVO.setPreviousNext(defaultPreviousNext);
        return policiesSearchVO;
    }

    @Override
    public PageVO<PoliciesSearchSimpleVO> latestCentralPolicies(PageQueryDTO pageQuery) throws Exception {
        return latestAreaPolicies(pageQuery, Collections.singletonList("AREA_ZY"), null);
    }

    @Override
    public PageVO<PoliciesSearchSimpleVO> latestLocalPolicies(PageQueryDTO pageQuery) throws Exception {
        return latestAreaPolicies(pageQuery, null, Arrays.asList("AREA_ZY", "AREA_DF(GS-S)"));
    }

    /**
     * 最新的区域政策
     * @param pageQuery 分页查询条件
     * @param include 包含的区域码值
     * @param exclude 排除的区域码值
     */
    public PageVO<PoliciesSearchSimpleVO> latestAreaPolicies(PageQueryDTO pageQuery, List<String> include, List<String> exclude) throws Exception {

        BoolQueryBuilder boolQuery = boolQuery();
        if (!CollectionUtils.isEmpty(include)) {
            boolQuery.must(termsQuery("area.codeValue", include));
        }
        if (!CollectionUtils.isEmpty(exclude)) {
            boolQuery.mustNot(termsQuery("area.codeValue", exclude));
        }

        // 执行查询
        SearchResponse response = simplePageSearch(getIndex(),
                boolQuery,
                pageQuery,
                SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC));

        // 数据映射
        SearchHits hits = response.getHits();
        List<PoliciesSearchSimpleVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            PoliciesSearchSimpleVO policiesSearchSimpleVO = objectMapper.readValue(hit.getSourceAsString(), PoliciesSearchSimpleVO.class);
            records.add(policiesSearchSimpleVO);
        }

        // 返回数据
        return new PageVO<PoliciesSearchSimpleVO>()
                .setTotal(hits.getTotalHits().value)
                .setPageNum(pageQuery.getPageNum())
                .setPageSize(pageQuery.getPageSize())
                .setRecords(records);
    }

    /**
     * 获取ES索引名称（其实是别名）
     * @return ES索引名称
     */
    private String getIndex() {
        PoliciesSearchQueryDTO queryDTO = new PoliciesSearchQueryDTO();
        return queryDTO.index();
    }
}
