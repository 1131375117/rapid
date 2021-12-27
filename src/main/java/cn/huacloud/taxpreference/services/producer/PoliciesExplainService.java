package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainListVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;

/**
 * 政策解读服务
 *
 * @author wuxin
 */
public interface PoliciesExplainService {

	/**
	 * 新增政策解读
	 *
	 * @param policiesExplainDTO 政策解读入参对象
	 * @param userId             用户id
	 */
	void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long userId);

	/**
	 * 校验当前政策法规id有没有其他政策解读关联
	 *
	 * @param policiesExplainDTO
	 */
	void checkAssociation(PoliciesExplainDTO policiesExplainDTO);

	/**
	 * 修改政策解读
	 *
	 * @param policiesExplainDTO 政策解读参数对象
	 */
	void updatePolicesExplain(PoliciesExplainDTO policiesExplainDTO);

	/**
	 * 根据id查询政策解读详情
	 *
	 * @param id 政策解读id
	 * @return 返回
	 */
	PoliciesExplainDetailVO getPoliciesById(Long id);

	/**
	 * 政策解读列表
	 *
	 * @param queryPoliciesExplainDTO 政策解读条件查询
	 * @return 返回
	 */
	PageVO<PoliciesExplainListVO> getPoliciesExplainList(QueryPoliciesExplainDTO queryPoliciesExplainDTO);


	/**
	 * 根据id删除政策解读
	 *
	 * @param id 政策解读id
	 */
	void deletePoliciesById(Long id);


	void deletePoliciesByPolicies(Long policiesId);


	/**
	 * 根据政策法规id查询政策解读信息
	 *
	 * @param policiesId 政策法规id
	 * @return 返回
	 */
	PoliciesExplainDTO getPoliciesByPoliciesId(Long policiesId);

	void updateSource(PoliciesExplainDTO policiesExplainDTO);
}
