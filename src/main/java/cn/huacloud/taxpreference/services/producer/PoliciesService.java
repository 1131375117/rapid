package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 政策服务
 * @author wuxin
 */
public interface PoliciesService {


    /**
     * 政策列表查询
     * @param queryDTO
     * @return
     */
    public IPage<PoliciesDO> getPolices(QueryDTO queryDTO);

    /**
     * 新增政策法规
     * @param policiesDTO
     * @param id
     */
    public void insertPolicies(PoliciesDTO policiesDTO, Long id);


    /**
     * 根据政策法规id获取详细信息
     * @param id
     * @return
     */
    public PoliciesVO getPoliciesById(Long id);


    /**
     * 修改政策法规
     * @param policiesDTO
     */
    public void updatePolicies(PoliciesDTO policiesDTO);
}
