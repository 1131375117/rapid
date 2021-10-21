package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesDTO;

import java.util.List;

/**
 * 政策服务
 * @author wuxin
 */
public interface PoliciesService {


    /**
     * 政策列表查询
     * @param policiesDO
     * @param page
     * @param size
     * @return
     */
    public List<PoliciesDO> getPolices(PoliciesDO policiesDO, Integer page, Integer size);

    /**
     * 新增政策法规
     * @param policiesDTO
     */
    public void insertPolicies(PoliciesDTO policiesDTO);


    /**
     * 根据政策法规id获取详细信息
     * @param id
     * @return
     */
    public PoliciesDO getPoliciesById(Long id);


    /**
     * 修改政策法规
     * @param policiesDO
     */
    public void updatePolicies(PoliciesDO policiesDO);
}
