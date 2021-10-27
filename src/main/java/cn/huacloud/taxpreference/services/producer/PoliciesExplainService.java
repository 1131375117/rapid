package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainVO;
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
     * @param id
     */
    public void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long id);

    /**
     * 修改政策解读
     * @param policiesExplainDTO
     */
    public void updatePolicesExplain(PoliciesExplainDTO policiesExplainDTO);

    /**
     * 根据id查询政策解读详情
     * @param id
     * @return
     */
    public PoliciesExplainDetailVO getPoliciesById(Long id);

    /**
     * 政策解读列表
     * @param queryPoliciesExplainDTO 政策解读条件查询
     * @return
     */
    public PageVO<PoliciesExplainVO> getPoliciesExplainList(QueryPoliciesExplainDTO queryPoliciesExplainDTO);


    /**
     * 根据id删除政策解读
     * @param id 政策解读id
     */
    public void deletePoliciesById(Long id);

    /**
     * 关联政策模糊查询
     * @param keywordPageQueryDTO 关联政策查询条件
     * @return
     */
    List<PoliciesTitleVO> fuzzyQuery(KeywordPageQueryDTO keywordPageQueryDTO);

}
