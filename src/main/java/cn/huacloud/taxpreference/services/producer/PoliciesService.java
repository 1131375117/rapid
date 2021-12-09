package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesCombinationDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryAbolishDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesCheckDeleteVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;

/**
 * 政策法规服务
 *
 * @author wuxin
 */
public interface PoliciesService {


	/**
	 * 政策列表查询
	 *
	 * @param queryPoliciesDTO 政策法规查询条件
	 * @return 返回
	 */
	PageVO<PoliciesVO> getPolicesList(QueryPoliciesDTO queryPoliciesDTO);

	/**
	 * 新增政策法规
	 *
	 * @param policiesCombinationDTO 政策法规参数集合
	 * @param userId                 录入人用户ID
	 */
	void savePolicies(PoliciesCombinationDTO policiesCombinationDTO, Long userId);


	/**
	 * 根据政策法规id获取详细信息
	 *
	 * @param id 政策法规id
	 * @return 返回
	 */
	PoliciesCombinationDTO getPoliciesById(Long id);


	/**
	 * 修改政策法规
	 *
	 * @param policiesCombinationDTO 政策法规参数集合
	 */
	void updatePolicies(PoliciesCombinationDTO policiesCombinationDTO);

	/**
	 * 校验删除政策法规
	 *
	 * @param id 政策法规id
	 * @return 返回
	 */
	PoliciesCheckDeleteVO checkDeletePoliciesById(Long id);

	/**
	 * 删除政策法规
	 *
	 * @param id 政策法规id
	 */
	void confirmDeletePoliciesById(Long id);

	/**
	 * 政策法规废止
	 *
	 * @param queryAbolishDTO 政策法规id
	 */
	void abolish(QueryAbolishDTO queryAbolishDTO);


	/**
	 * 查询废止信息
	 *
	 * @param id 政策法规id
	 * @return 返回
	 */
	PoliciesAbolishVO getAbolish(Long id);


	/**
	 * 校验标题和文号是否重复
	 * @param titleOrDocCode 标题或文号
	 * @return 返回 true 重复 false 不重复
	 */
	Boolean checkTitleAndDocCode(String titleOrDocCode);


	/**
	 * 根据政策法规id查询政策法规信息
	 * @param id 政策法规id
	 * @return 返回政策法规对象
	 */
	PoliciesDO getPolicies(Long id);

	/**
	 * 关联政策模糊查询
	 * @param keywordPageQueryDTO 分页条件
	 * @return
	 */
	PageVO<PoliciesTitleVO> fuzzyQuery(KeywordPageQueryDTO keywordPageQueryDTO);

}
