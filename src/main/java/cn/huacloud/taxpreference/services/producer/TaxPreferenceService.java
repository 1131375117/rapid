package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPrefrencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPrefrencesVO;
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
     *修改税收优惠查询基本信息接口
     * @param queryTaxPrefrencesDTO
     * @return queryTaxPrefrencesVO
     */
    ResultVO<PageVO<QueryTaxPrefrencesVO>> queryTaxPreferenceList(QueryTaxPrefrencesDTO queryTaxPrefrencesDTO);
}
