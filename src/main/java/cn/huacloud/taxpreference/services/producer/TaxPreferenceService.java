package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryAbolishDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPreferencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceIdsDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPreferencesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceAbolishVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceVO;

import java.util.List;


/**
 * 优惠政策服务
 *
 * @author fuhua
 */
public interface TaxPreferenceService {
    /**
     * 新增税收优惠接口
     *
     * @param taxPreferenceDTO
     * @return resultVO
     */
    ResultVO<Void> insertTaxPreference(TaxPreferenceDTO taxPreferenceDTO);

    /**
     * 修改税收优惠接口
     *
     * @param taxPreferenceDTO
     * @return resultVO
     */
    ResultVO<Void> updateTaxPreference(TaxPreferenceDTO taxPreferenceDTO);

    /**
     * 修改税收优惠查询基本信息接口
     *
     * @param id
     * @return taxPreferenceVO
     */
    ResultVO<TaxPreferenceVO> queryTaxPreferenceInfo(Long id);

    /**
     * 查询税收优惠查询基本信息接口
     *
     * @param queryTaxPreferencesDTO
     * @param userId
     * @return queryTaxPreferencesVO
     */
    ResultVO<PageVO<QueryTaxPreferencesVO>> queryTaxPreferenceList(QueryTaxPreferencesDTO queryTaxPreferencesDTO, Long userId);

    /**
     * 税收优惠实现删除接口
     *
     * @param ids
     * @return Void
     */
    ResultVO<Void> deleteTaxPreference(Long[] ids);

    /**
     * 内容撤回
     *
     * @param id
     * @return Void
     */
    ResultVO<Void> reTaxPreference(Long id);


    /**
     * 根据政策法规废止状态修改税收优惠状态
     * @param queryAbolishDTO 条件
     * @return
     */
    void updateStatus(QueryAbolishDTO queryAbolishDTO);

    /**
     * 查询税收优惠废止的信息
     * @param policiesId 政策法规id
     * @return
     */
    List<TaxPreferenceAbolishVO> getTaxPreferenceAbolish(Long policiesId);

    /**
     * 删除税收优惠关联表
     * @param id 政策法规id
     */
    void deleteTaxPreferencePolicies(Long id);

}
