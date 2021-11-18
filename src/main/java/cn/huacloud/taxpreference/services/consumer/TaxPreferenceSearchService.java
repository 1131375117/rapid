package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.services.consumer.entity.dtos.TaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.HotLabelVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.TaxPreferenceSearchVO;

import java.util.List;

/**
 * 税收优惠检索服务
 * @author wangkh
 */
public interface TaxPreferenceSearchService extends SearchService<TaxPreferenceSearchQueryDTO, TaxPreferenceSearchVO> {
    /**
     * 热门标签列表
     * @param size 数据大小
     * @return 热门标签
     */
    List<HotLabelVO> hotLabels(Integer size);

    /**
     * 税收优惠详情
     * @param id 税收优惠ID
     * @return 税收优惠详情
     */
    TaxPreferenceSearchVO getTaxPreferenceDetails(Long id) throws Exception;
}
