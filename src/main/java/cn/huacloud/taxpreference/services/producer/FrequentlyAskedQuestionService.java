package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;

import java.util.List;

/**
 * 热点问答服务
 *
 * @author wuxin
 */
public interface FrequentlyAskedQuestionService {


    /**
     * 新增热点问答
     *
     * @param frequentlyAskedQuestionDTOS
     * @param userId
     */
    void insertFrequentlyAskedQuestion(List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOS, Long userId);

    /**
     * 修改热点问答
     *
     * @param frequentlyAskedQuestionDTOS
     */
    void updateFrequentlyAskedQuestion(List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOS);

    /**
     * 热点问答列表查询
     *
     * @param queryPoliciesExplainDTO 查询条件
     * @return
     */
    PageVO<PoliciesExplainDetailVO> getFrequentlyAskedQuestionList(QueryPoliciesExplainDTO queryPoliciesExplainDTO);

    /**
     * 删除热点问答
     * @param id
     */
    void deleteFrequentlyAskedQuestion(Long id);
}
