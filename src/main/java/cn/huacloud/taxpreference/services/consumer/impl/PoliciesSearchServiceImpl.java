package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.services.consumer.PoliciesSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.List;
import java.util.Map;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
//@Service
public class PoliciesSearchServiceImpl implements PoliciesSearchService {

    @Getter
    private final RestHighLevelClient restHighLevelClient;

    private final ObjectMapper objectMapper;

    @Override
    public QueryBuilder getQueryBuilder(PoliciesSearchQueryDTO pageQuery) {
        BoolQueryBuilder queryBuilder = boolQuery();

        // 关键字查询
        String keyword = pageQuery.getKeyword();
        if (keyword != null) {
            List<String> searchFields = pageQuery.searchFields();
            for (String searchField : searchFields) {
                queryBuilder.should(matchPhraseQuery(searchField, keyword));
            }
        }

        // 固定条件过滤


        return queryBuilder;
    }

    @Override
    public PoliciesSearchVO mapSearchHit(SearchHit searchHit) throws Exception {
        PoliciesSearchVO policiesSearchVO = objectMapper.readValue(searchHit.getSourceAsString(), PoliciesSearchVO.class);

        // 设置高亮字段
        policiesSearchVO.setTitle(getHighlightString(searchHit, "title"));
        policiesSearchVO.setContent(getHighlightString(searchHit, "content"));

        return policiesSearchVO;
    }
}
