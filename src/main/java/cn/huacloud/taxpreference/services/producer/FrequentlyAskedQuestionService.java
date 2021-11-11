package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.FrequentlyAskedQuestionVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;

import java.util.List;

/**
 * 热门问答服务
 *
 * @author wuxin
 */
public interface FrequentlyAskedQuestionService {


    /**
     * 新增热门问答
     *
     * @param frequentlyAskedQuestionDTOS
     * @param userId
     */
    void insertFrequentlyAskedQuestion(List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOS, Long userId);

    /**
     * 修改热门问答
     *
     * @param frequentlyAskedQuestionDTOS
     */
    void updateFrequentlyAskedQuestion(List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOS);

    /**
     * 热门问答列表查询
     *
     * @param queryPoliciesExplainDTO 查询条件
     * @return
     */
    PageVO<PoliciesExplainDetailVO> getFrequentlyAskedQuestionList(QueryPoliciesExplainDTO queryPoliciesExplainDTO);

    /**
     * 删除热门问答
     * @param id
     */
    void deleteFrequentlyAskedQuestion(Long id);

    /**
     * 根据政策法规id查询热门问答信息
     * @param policiesId
     * @return
     */
    List<FrequentlyAskedQuestionDTO> getFrequentlyAskedQuestionByPoliciesId(Long policiesId);

    /**
     * 根据热门问答id查询详情
     * @param id
     * @return
     */
    PoliciesExplainDetailVO getFrequentlyAskedQuestionById(Long id);
}
