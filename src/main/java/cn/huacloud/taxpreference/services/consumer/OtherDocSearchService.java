package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;

/**
 * @author fuhua
 **/
public interface OtherDocSearchService extends SearchService<OtherDocDTO,OtherDocVO>{

    OtherDocVO getTaxOtherDocDetails(Long id) throws Exception;
}
