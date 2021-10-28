package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.PoliciesCombinationDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryAbolishDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesDetailVO;
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
     * @return
     */
     PageVO<PoliciesVO> getPolicesList(QueryPoliciesDTO queryPoliciesDTO);

    /**
     * 新增政策法规
     *
     * @param policiesCombinationDTO 政策法规参数集合
     * @param id              录入人用户ID
     */
     void insertPolicies(PoliciesCombinationDTO policiesCombinationDTO, Long id);


    /**
     * 根据政策法规id获取详细信息
     *
     * @param id 政策法规id
     * @return
     */
     PoliciesDetailVO getPoliciesById(Long id);


    /**
     * 修改政策法规
     *
     * @param policiesCombinationDTO 政策法规参数集合
     */
     void updatePolicies(PoliciesCombinationDTO policiesCombinationDTO);

    /**
     * 删除政策法规
     *
     * @param id 政策法规id
     */
     void deletePoliciesById(Long id);

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
     * @return
     */
     PoliciesAbolishVO getAbolish(Long id);
}
