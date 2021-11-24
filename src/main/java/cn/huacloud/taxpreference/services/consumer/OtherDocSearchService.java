package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.OtherDocDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.OtherDocVO;

/**
 * @author fuhua
 **/
public interface OtherDocSearchService {
    PageVO<OtherDocVO> pageSearch(OtherDocDTO pageQuery);
}
