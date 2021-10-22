package cn.huacloud.taxpreference.services;

import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesExplainDTO;

/**
 * @author wuxin
 */
public interface PoliciesExplainService {

    /**
     * 新增政策解读
     * @param
     * @param policiesExplainDTO
     * @param id
     */
    public void insertPoliciesExplain(PoliciesExplainDTO policiesExplainDTO, Long id);
}
