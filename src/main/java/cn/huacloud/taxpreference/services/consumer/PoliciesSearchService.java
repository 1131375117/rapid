package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchListVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchVO;

/**
 * 政策法规检索服务
 * @author wangkh
 */
public interface PoliciesSearchService extends SearchService<PoliciesSearchQueryDTO, PoliciesSearchListVO> {

    /**
     * 获取详情
     * @param id 政策法规ID
     * @return 政策详情
     */
    PoliciesSearchVO getPoliciesDetails(Long id) throws Exception;

    /**
     * 最新中央政策
     * @param pageQuery 分页查询条件
     * @return 分页数据
     */
    PageVO<PoliciesSearchSimpleVO> latestCentralPolicies(PageQueryDTO pageQuery) throws Exception;

    /**
     * 最新地方政策
     * @param pageQuery 分页查询条件
     * @return 分页数据
     */
    PageVO<PoliciesSearchSimpleVO> latestLocalPolicies(PageQueryDTO pageQuery) throws Exception;

    /**
     * 热门政策（首页）
     * @param pageQuery
     * @return
     */
    PageVO<PoliciesSearchSimpleVO> hotPolicies(PageQueryDTO pageQuery) throws Exception;
}
