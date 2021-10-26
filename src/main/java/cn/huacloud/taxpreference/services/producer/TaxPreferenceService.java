package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPreferencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPreferencesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceVO;


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

    /**
     *修改税收优惠查询基本信息接口
     * @param id
     * @return taxPreferenceVO
     */
    ResultVO<TaxPreferenceVO> queryTaxPreferenceInfo(Long id);

    /**
     *查询税收优惠查询基本信息接口
     * @param queryTaxPreferencesDTO
     * @param userId
     * @return queryTaxPreferencesVO
     */
    ResultVO<PageVO<QueryTaxPreferencesVO>> queryTaxPreferenceList(QueryTaxPreferencesDTO queryTaxPreferencesDTO, Long userId);

    /**
     * 税收优惠实现删除接口
     * @param ids
     * @return Void
     */
    ResultVO<Void> deleteTaxPreference(Long[] ids);
    /**
     * 内容撤回
     * @param id
     * @return Void
     */
    ResultVO<Void> reTaxPreference(Long id);
}
