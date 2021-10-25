package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesListDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 政策法规服务
 *
 * @author wuxin
 */
public interface PoliciesService {


    /**
     * 政策列表查询
     *
     * @param queryDTO
     * @return
     */
    public IPage<PoliciesDO> getPolices(QueryDTO queryDTO);

    /**
     * 新增政策法规
     *
     * @param policiesListDTO
     * @param id
     */
    public void insertPolicies(PoliciesListDTO policiesListDTO, Long id);


    /**
     * 根据政策法规id获取详细信息
     *
     * @param id
     * @return
     */
    public PoliciesVO getPoliciesById(Long id);


    /**
     * 修改政策法规
     *
     * @param policiesListDTO
     */
    public void updatePolicies(PoliciesListDTO policiesListDTO);
}
