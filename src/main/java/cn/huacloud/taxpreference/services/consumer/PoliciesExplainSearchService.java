package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesExplainSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchListVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesExplainSearchVO;

import java.util.List;

/**
 * 政策解读检索服务
 * @author wangkh
 */
public interface PoliciesExplainSearchService extends SearchService<PoliciesExplainSearchQueryDTO, PoliciesExplainSearchListVO> {

    /**
     * 政策解读详情
     * @param id 政策解读ID
     * @return 政策解读详情
     */
    PoliciesExplainSearchVO getPoliciesExplainDetails(Long id) throws Exception;

    /**
     * 最新政策解读
     * @param pageQuery 分页查询条件
     * @return 分页数据
     */
    PageVO<PoliciesExplainSearchSimpleVO> latestPoliciesExplain(PageQueryDTO pageQuery) throws Exception;

    /**
     * 根据政策ID查询相关政策解读
     * @param policiesId 政策法规ID
     * @return 政策解读简单视图
     */
    List<PoliciesExplainSearchSimpleVO> policiesRelatedExplain(String policiesId) throws Exception;
}
