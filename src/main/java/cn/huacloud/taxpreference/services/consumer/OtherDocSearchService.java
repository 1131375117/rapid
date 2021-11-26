package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocQueryDTO;
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
}
