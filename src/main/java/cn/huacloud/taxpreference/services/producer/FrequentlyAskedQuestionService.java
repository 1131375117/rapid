package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;

/**
 * 热点问答服务
 *
 * @author wuxin
 */
public interface FrequentlyAskedQuestionService {

    /**
     * 新增热点问答
     *
     * @param frequentlyAskedQuestionDTO
     * @param id
     */
    public void insertFrequentlyAskedQuestion(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO, Long id);

}
