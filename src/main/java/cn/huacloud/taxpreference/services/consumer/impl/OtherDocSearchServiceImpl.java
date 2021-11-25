package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.services.consumer.OtherDocSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PreviousNextVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

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
        return new String[]{"htmlContent", "plainContent"};
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

    private String getIndex() {
        OtherDocDTO queryDTO = new OtherDocDTO();
        return queryDTO.index();
    }
}
