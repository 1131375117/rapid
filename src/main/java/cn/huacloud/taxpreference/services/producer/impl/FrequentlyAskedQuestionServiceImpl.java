package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.services.producer.FrequentlyAskedQuestionService;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 热点问答服务实现类
 *
 * @author wuxin
 */
@RequiredArgsConstructor
@Service
public class FrequentlyAskedQuestionServiceImpl implements FrequentlyAskedQuestionService {

    private final FrequentlyAskedQuestionMapper frequentlyAskedQuestionMapper;

    /**
     * 新增热点问答
     *
     * @param frequentlyAskedQuestionDTO
     * @param id
     */
    @Override
    public void insertFrequentlyAskedQuestion(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO, Long id) {
        FrequentlyAskedQuestionDO frequentlyAskedQuestionDO = new FrequentlyAskedQuestionDO();
        BeanUtils.copyProperties(frequentlyAskedQuestionDTO, frequentlyAskedQuestionDO);
        frequentlyAskedQuestionDO.setInputUserId(id);
        frequentlyAskedQuestionDO.setCreateTime(LocalDateTime.now());
        frequentlyAskedQuestionDO.setUpdateTime(LocalDateTime.now());
        frequentlyAskedQuestionDO.setDeleted(0);
        frequentlyAskedQuestionMapper.insert(frequentlyAskedQuestionDO);
    }
}
