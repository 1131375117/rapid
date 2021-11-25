package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.services.consumer.OtherDocSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

}
