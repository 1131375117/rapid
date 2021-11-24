package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.OtherDocSearchService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 案例分析实现类
 *
 * @author fuhua
 **/
@Service
@RequiredArgsConstructor
public class OtherDocSearchServiceImpl implements OtherDocSearchService {

    private final RestHighLevelClient restHighLevelClient;

    @Override
    public PageVO<OtherDocVO> pageSearch(OtherDocDTO pageQuery) {
        List<String> searchFields = pageQuery.searchFields();

        return null;
    }
}
