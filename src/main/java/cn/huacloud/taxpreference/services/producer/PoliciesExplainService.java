package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesTitleVO;

import java.util.List;

/**
 * 政策解读服务
 *
 * @author wuxin
 */
public interface PoliciesExplainService {

    /**
     * 新增政策解读
     *
     * @param
     * @param policiesExplainDTO
     * @param userId
     */
     void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long userId);

    /**
     * 修改政策解读
     *
     * @param policiesExplainDTO
     */
     void updatePolicesExplain(PoliciesExplainDTO policiesExplainDTO);

    /**
     * 根据id查询政策解读详情
     *
     * @param id
     * @return
     */
     PoliciesExplainDetailVO getPoliciesById(Long id);

    /**
     * 政策解读列表
     *
     * @param queryPoliciesExplainDTO 政策解读条件查询
     * @return
     */
     PageVO<PoliciesExplainDetailVO> getPoliciesExplainList(QueryPoliciesExplainDTO queryPoliciesExplainDTO);


    /**
     * 根据id删除政策解读
     *
     * @param id 政策解读id
     */
     void deletePoliciesById(Long id);

    /**
     * 关联政策模糊查询
     *
     * @return
     */
    List<PoliciesTitleVO> fuzzyQuery(KeywordPageQueryDTO keywordPageQueryDTO);


    /**
     * 根据政策法规id查询政策解读信息
     * @param policiesId 政策法规id
     * @return
     */
    PoliciesExplainDTO getPoliciesByPoliciesId(Long policiesId);
}
