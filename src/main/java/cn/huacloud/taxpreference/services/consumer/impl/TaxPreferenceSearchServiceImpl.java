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

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class TaxPreferenceSearchServiceImpl implements TaxPreferenceSearchService {

    @Getter
    private final RestHighLevelClient restHighLevelClient;

    private final ObjectMapper objectMapper;

    @Override
    public List<HotLabelVO> hotLabels(Integer size) {

        return null;
    }

    @Override
    public QueryBuilder getQueryBuilder(TaxPreferenceSearchQueryDTO pageQuery) {
        BoolQueryBuilder queryBuilder = generatorDefaultQueryBuilder(pageQuery);

        return null;
    }

    @Override
    public TaxPreferenceSearchVO getTaxPreferenceDetails(Long id) {
        return null;
    }

    @Override
    public TaxPreferenceSearchVO mapSearchHit(SearchHit searchHit) throws Exception {
        TaxPreferenceSearchVO taxPreferenceSearchVO = objectMapper.readValue(searchHit.getSourceAsString(), TaxPreferenceSearchVO.class);
        return null;
    }
}
