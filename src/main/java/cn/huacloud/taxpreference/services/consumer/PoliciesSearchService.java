package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.services.consumer.entity.dtos.PoliciesSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesSearchVO;

/**
 * 正常法规检索服务
 * @author wangkh
 */
public interface PoliciesSearchService extends SearchService<PoliciesSearchQueryDTO, PoliciesSearchVO> {
}
