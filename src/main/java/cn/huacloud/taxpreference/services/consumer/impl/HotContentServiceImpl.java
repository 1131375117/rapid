package cn.huacloud.taxpreference.services.consumer.impl;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.HotContentService;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.GuessYouLikeQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotContentVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class HotContentServiceImpl implements HotContentService {

    private final RestHighLevelClient restHighLevelClient;

    private final ObjectMapper objectMapper;

    @Override
    public PageVO<HotContentVO> weeklyHotContent(PageQueryDTO pageQuery) {

        return new PageVO<HotContentVO>().setRecords(new ArrayList<>());
    }

    @Override
    public PageVO<HotContentVO> guessYouLike(GuessYouLikeQueryDTO pageQuery) {

        return new PageVO<HotContentVO>().setRecords(new ArrayList<>());
    }
}
