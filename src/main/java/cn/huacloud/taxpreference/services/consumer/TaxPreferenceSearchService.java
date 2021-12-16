package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.LatestTaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.TaxPreferenceSearchQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.DocSearchSimpleVO;
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
    List<HotLabelVO> hotLabels(Integer size) throws Exception;

    /**
     * 税收优惠详情
     * @param id 税收优惠ID
     * @return 税收优惠详情
     */
    TaxPreferenceSearchVO getTaxPreferenceDetails(Long id) throws Exception;

    /**
     * 最新税收优惠（首页）
     * @param pageQuery
     * @return
     */
    PageVO<DocSearchSimpleVO> latestTaxPreference(LatestTaxPreferenceSearchQueryDTO pageQuery) throws Exception;

    /**
     * 热门税收优惠（首页）
     * @param pageQuery
     * @return
     */
    PageVO<DocSearchSimpleVO> hotTaxPreference(PageQueryDTO pageQuery) throws Exception;
}
