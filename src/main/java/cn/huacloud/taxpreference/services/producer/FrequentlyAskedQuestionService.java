package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.FrequentlyAskedQuestionQueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.FrequentlyAskedQuestionDetailVO;
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
	 * @param frequentlyAskedQuestionDtO 热门问答入参对象
	 */
	void insertFrequentlyAskedQuestion(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDtO, Long userId);

	/**
	 * 修改热门问答
	 *
	 * @param frequentlyAskedQuestionDTO 热门问答入参对象
	 */
	void updateFrequentlyAskedQuestion(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDTO);

	/**
	 * 热门问答列表查询
	 *
	 * @param frequentlyAskedQuestionQueryDTO 查询条件
	 * @return 返回
	 */
	PageVO<FrequentlyAskedQuestionVO> getFrequentlyAskedQuestionList(FrequentlyAskedQuestionQueryDTO frequentlyAskedQuestionQueryDTO);

	/**
	 * 删除热门问答
	 *
	 * @param id 热门问答id
	 */
	void deleteFrequentlyAskedQuestion(Long id);

	/**
	 * 根据政策法规id查询热门问答信息
	 *
	 * @param policiesId 政策法规id
	 * @return 返回
	 */
	List<FrequentlyAskedQuestionDTO> getFrequentlyAskedQuestionByPoliciesId(Long policiesId);

	/**
	 * 根据热门问答id查询详情
	 *
	 * @param id 热门问答id
	 * @return 返回
	 */
	FrequentlyAskedQuestionDetailVO getFrequentlyAskedQuestionById(Long id);

	/**
	 * 热门问答数据加工
	 * @param frequentlyAskedQuestionDto
	 */
	void updateDataProcessing(FrequentlyAskedQuestionDTO frequentlyAskedQuestionDto);
}
