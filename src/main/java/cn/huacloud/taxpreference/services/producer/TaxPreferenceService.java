package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import org.springframework.stereotype.Service;


/**
 * 优惠政策服务
 * @author fuhua
 */
public interface TaxPreferenceService {
    /**
     * 新增税收优惠接口
     * @param taxPreferenceDTO
     * @return resultVO
     */
    ResultVO<Void> insertTaxPreference(TaxPreferenceDTO taxPreferenceDTO);

    /**
     * 修改税收优惠接口
     * @param taxPreferenceDTO
     * @return resultVO
     */
    ResultVO<Void> updateTaxPreference(TaxPreferenceDTO taxPreferenceDTO);
}
