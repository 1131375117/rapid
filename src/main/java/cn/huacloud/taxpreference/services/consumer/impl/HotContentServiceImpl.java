package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import cn.huacloud.taxpreference.services.consumer.HotContentService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.GuessYouLikeQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotContentVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class HotContentServiceImpl implements HotContentService {

    private final RestHighLevelClient restHighLevelClient;

    private final ElasticsearchIndexConfig indexConfig;

    private final ObjectMapper objectMapper;

    @Override
    public PageVO<HotContentVO> weeklyHotContent(PageQueryDTO pageQuery) throws Exception {

        // static param
        String[] indices = getIndices(Arrays.asList(DocType.POLICIES,
                DocType.POLICIES_EXPLAIN,
                DocType.FREQUENTLY_ASKED_QUESTION,
                DocType.TAX_PREFERENCE,
                DocType.CASE_ANALYSIS));

        String[] fetchSource = {"id", "docType.codeName", "docType.codeValue", "title", "releaseDate", "labels"};

        // source builder
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .fetchSource(fetchSource, null)
                .from(pageQuery.from())
                .size(pageQuery.getPageSize())
                .sort("releaseDate", SortOrder.DESC);

        // request builder
        SearchRequest request = new SearchRequest(indices)
                .source(searchSourceBuilder);

        // do search
        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);

        SearchHits hits = response.getHits();

        List<HotContentVO> records = new ArrayList<>();
        for (SearchHit hit : hits.getHits()) {
            String index = hit.getIndex();
            HotContentVO hotContentVO = objectMapper.readValue(hit.getSourceAsString(), HotContentVO.class);
            setDocType(index, hotContentVO);
            records.add(hotContentVO);
        }

        return new PageVO<HotContentVO>().setPageNum(pageQuery.getPageNum()).setPageSize(pageQuery.getPageSize())
                .setRecords(new ArrayList<>());
    }

    @Override
    public PageVO<HotContentVO> guessYouLike(GuessYouLikeQueryDTO pageQuery) {
        DocType docType = pageQuery.getDocType();
        if (docType == null) {
            // 查询所有

        } else {
            // 查询
        }
        return new PageVO<HotContentVO>().setRecords(new ArrayList<>());
    }

    /**
     * 获取查询的索引
     * @param docTypes
     * @return
     */
    private String[] getIndices(List<DocType> docTypes) {
        return docTypes.stream()
                .map(docType -> docType.indexGetter.apply(indexConfig).getAlias())
                .distinct()
                .collect(Collectors.toList())
                .toArray(new String[]{});
    }

    /**
     * 设置文档类型
     * @param index 索引名称
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
