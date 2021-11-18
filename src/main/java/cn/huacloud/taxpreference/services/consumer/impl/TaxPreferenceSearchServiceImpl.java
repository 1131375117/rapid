package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.services.consumer.TaxPreferenceSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.TaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotLabelVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.TaxPreferenceSearchVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

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
    public List<HotLabelVO> hotLabels(Integer size) {

        return null;
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

        return queryBuilder;
    }

    @Override
    public TaxPreferenceSearchVO getTaxPreferenceDetails(Long id) {
        return null;
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
