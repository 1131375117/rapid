package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;

/**
 * @author fuhua
 **/
public interface OtherDocSearchService extends SearchService<OtherDocDTO,OtherDocVO>{
    /**
     * 税收优惠详情
     * @param id
     * @return
     * @throws Exception
     */
    OtherDocVO getTaxOtherDocDetails(Long id) throws Exception;
}
