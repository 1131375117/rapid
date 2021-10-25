package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.SubmitConditionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryTaxPreferencesDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.SubmitConditionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferencePoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryTaxPreferencesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.SubmitConditionVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferencePoliciesVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceVO;
import cn.huacloud.taxpreference.services.producer.mapper.SubmitConditionMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferencePoliciesMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 优惠政策服务实现类
 * @author: fuhua
 * @create: 2021-10-21 10:36
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class TaxPreferenceServiceImpl implements TaxPreferenceService {

    private final TaxPreferenceMapper taxPreferenceMapper;

    private final TaxPreferencePoliciesMapper taxPreferencePoliciesMapper;

    private final SubmitConditionMapper submitConditionMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> insertTaxPreference(TaxPreferenceDTO taxPreferenceDTO) {
        //新增-税收优惠表t_tax_preference
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(taxPreferenceDTO);
        taxPreferenceDO.setCreateTime(LocalDateTime.now());
        taxPreferenceMapper.insert(taxPreferenceDO);

        //新增-税收优惠政策法规关联表t_tax_preference_ policies
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOList = getTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
        for (TaxPreferencePoliciesDO taxPreferencePoliciesDO : taxPreferencePoliciesDOList) {
            taxPreferencePoliciesMapper.insert(taxPreferencePoliciesDO);
        }

        //新增-申报条件表 t_submit_condition
        insertSubmitConditionDOs(taxPreferenceDTO, taxPreferenceDO);
        return ResultVO.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> updateTaxPreference(TaxPreferenceDTO taxPreferenceDTO) {
        //修改-税收优惠表t_tax_preference
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(taxPreferenceDTO);
        taxPreferenceMapper.updateById(taxPreferenceDO);

        //修改-税收优惠政策法规关联表t_tax_preference_ policies
        List<TaxPreferencePoliciesDO> preferencePoliciesDOList = getTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
        for (TaxPreferencePoliciesDO preferencePoliciesDO : preferencePoliciesDOList) {
            taxPreferencePoliciesMapper.updateByTaxPreferenceId(preferencePoliciesDO);
        }

        //修改-申报条件表 t_submit_condition
        updateSubmitConditionByTaxPreferenceId(taxPreferenceDTO);
        return ResultVO.ok();
    }

    @Override
    public ResultVO<TaxPreferenceVO> queryTaxPreferenceInfo(Long id) {
        TaxPreferenceVO taxPreferenceVO = new TaxPreferenceVO();
        Map<String, Object> columnMap = new HashMap<>(16);
        columnMap.put("tax_preference_id", id);

        //根据id查询税收优惠表t_tax_preference信息
        TaxPreferenceDO taxPreferenceDO = taxPreferenceMapper.selectById(id);
        BeanUtils.copyProperties(taxPreferenceDO, taxPreferenceVO);

        //查询政策信息
        List<TaxPreferencePoliciesVO> taxPreferencePoliciesVOList = getTaxPreferencePoliciesVOS(columnMap);
        taxPreferenceVO.setTaxPreferencePoliciesVOList(taxPreferencePoliciesVOList);

        //查询申报信息
        List<SubmitConditionDO> submitConditionDOList = submitConditionMapper.selectByMap(columnMap);
        List<SubmitConditionVO> submitConditionVOList = getSubmitConditionVOS(submitConditionDOList);
        taxPreferenceVO.setSubmitConditionVOList(submitConditionVOList);

        return ResultVO.ok(taxPreferenceVO);
    }

    @Override
    public ResultVO<PageVO<QueryTaxPreferencesVO>> queryTaxPreferenceList(QueryTaxPreferencesDTO queryTaxPreferencesDTO) {
        Page<QueryTaxPreferencesVO> page = new Page<>(queryTaxPreferencesDTO.getPageNum(), queryTaxPreferencesDTO.getPageSize());
        IPage<QueryTaxPreferencesVO> iPage = taxPreferenceMapper.queryTaxPreferenceVOList(page, queryTaxPreferencesDTO);
        PageVO<QueryTaxPreferencesVO> pageVO = PageVO.createPageVO(iPage, iPage.getRecords());
        return ResultVO.ok(pageVO);
    }

    /**
     * 获取申报信息
     *
     * @return submitConditionVO
     */
    private List<SubmitConditionVO> getSubmitConditionVOS(List<SubmitConditionDO> submitConditionDOS) {
        List<SubmitConditionVO> submitConditionVOList = new ArrayList<>();
        submitConditionDOS.forEach(submitConditionDO ->
                {
                    SubmitConditionVO submitConditionVO = new SubmitConditionVO();
                    BeanUtils.copyProperties(submitConditionDO, submitConditionVO);
                    submitConditionVOList.add(submitConditionVO);
                }
        );
        return submitConditionVOList;
    }

    /**
     * 获取政策法规信息
     *
     * @return taxPreferencePoliciesVO
     */
    private List<TaxPreferencePoliciesVO> getTaxPreferencePoliciesVOS(Map<String, Object> columnMap) {
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOS = taxPreferencePoliciesMapper.selectByMap(columnMap);
        List<TaxPreferencePoliciesVO> taxPreferencePoliciesVOList = new ArrayList<>();
        taxPreferencePoliciesDOS.forEach(taxPreferencePoliciesDO ->
                {
                    TaxPreferencePoliciesVO taxPreferencePoliciesVO = new TaxPreferencePoliciesVO();
                    BeanUtils.copyProperties(taxPreferencePoliciesDO, taxPreferencePoliciesVO);
                    taxPreferencePoliciesVOList.add(taxPreferencePoliciesVO);
                }
        );
        return taxPreferencePoliciesVOList;
    }

    /**
     * 修改税收优惠申报条件
     */
    private void updateSubmitConditionByTaxPreferenceId(TaxPreferenceDTO taxPreferenceDTO) {
        //采取先删除后添加的方式
        HashMap<String, Object> columnMap = new HashMap<>(16);
        columnMap.put("tax_preference_id", taxPreferenceDTO.getId());
        submitConditionMapper.deleteByMap(columnMap);
        TaxPreferenceDO taxPreferenceDO = getPreferenceDO(taxPreferenceDTO);
        insertSubmitConditionDOs(taxPreferenceDTO, taxPreferenceDO);
    }

    private TaxPreferenceDO getPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        taxPreferenceDO.setId(taxPreferenceDTO.getId());
        return taxPreferenceDO;
    }

    /**
     * 新增到申报条件表t_submit_condition
     */
    public void insertSubmitConditionDOs(TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        List<SubmitConditionDTO> submitConditionDTOList = taxPreferenceDTO.getSubmitConditionDTOList();
        if (submitConditionDTOList != null && submitConditionDTOList.size() > 0) {
            for (int i = 0; i < submitConditionDTOList.size(); i++) {
                SubmitConditionDO submitConditionDO = new SubmitConditionDO();
                BeanUtils.copyProperties(submitConditionDTOList.get(i), submitConditionDO);
                submitConditionDO.setTaxPreferenceId(taxPreferenceDO.getId());
                submitConditionDO.setSort((long) i+1);
                submitConditionMapper.insert(submitConditionDO);
            }
        }
    }

    /**
     * 填充TaxPreferencePoliciesDO对象
     */
    private List<TaxPreferencePoliciesDO> getTaxPreferencePoliciesDO(TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {

        TaxPreferencePoliciesDO preferencePoliciesDO = new TaxPreferencePoliciesDO();
        //政策法规对象集合
        List<TaxPreferencePoliciesDO> taxPreferencePoliciesDOList = new ArrayList<>();
        List<TaxPreferencePoliciesDTO> taxPreferencePoliciesDTOList = taxPreferenceDTO.getTaxPreferencePoliciesDTOList();
        if (taxPreferencePoliciesDTOList != null && taxPreferencePoliciesDTOList.size() > 0) {
            for (int i = 0; i < taxPreferencePoliciesDTOList.size(); i++) {
                BeanUtils.copyProperties(taxPreferencePoliciesDTOList.get(i), preferencePoliciesDO);
                preferencePoliciesDO.setTaxPreferenceId(taxPreferenceDO.getId());
                preferencePoliciesDO.setSort((long) i+1);
                taxPreferencePoliciesDOList.add(preferencePoliciesDO);
            }
        }
        return taxPreferencePoliciesDOList;
    }

    /**
     * 填充taxPreferenceDO对象
     */
    private TaxPreferenceDO getTaxPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        BeanUtils.copyProperties(taxPreferenceDTO, taxPreferenceDO);
        taxPreferenceDO.setDeleted(false);
        taxPreferenceDO.setUpdateTime(LocalDateTime.now());
        taxPreferenceDO.setValidity(PreferenceValidation.EFFECTIVE.getValue());
        return taxPreferenceDO;
    }


}
