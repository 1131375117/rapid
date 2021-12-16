package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.DocSearchSimpleVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;

/**
 * @author fuhua
 **/
public interface OtherDocSearchService extends SearchService<OtherDocQueryDTO, OtherDocVO> {
    /**
     * 税收优惠详情
     *
     * @param id
     * @return
     * @throws Exception
     */
    OtherDocVO getTaxOtherDocDetails(Long id) throws Exception;

    /**
     * 最新案例解析（首页）
     * @param pageQuery
     * @return
     */
    PageVO<DocSearchSimpleVO> latestCaseAnalyse(PageQueryDTO pageQuery) throws Exception;
}
