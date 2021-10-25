package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesExplainDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainDetailVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesExplainVO;

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
     * @param queryPoliciesExplainDTO
     * @return
     */
    public PageVO<PoliciesExplainDO> getPoliciesExplainList(QueryPoliciesExplainDTO queryPoliciesExplainDTO);
}
