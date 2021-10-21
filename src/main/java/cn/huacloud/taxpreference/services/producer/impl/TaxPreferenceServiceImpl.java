package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.SubmitConditionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.SubmitConditionDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.TaxPreferenceDTO;
import cn.huacloud.taxpreference.services.producer.mapper.SubmitConditionMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferencePoliciesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @description: 优惠政策服务实现类
 * @author: fuhua
 * @create: 2021-10-21 10:36
 **/
@Slf4j
@Service
public class TaxPreferenceServiceImpl implements TaxPreferenceService {
    @Autowired
    private TaxPreferenceMapper taxPreferenceMapper;
    @Autowired
    private TaxPreferencePoliciesMapper taxPreferencePoliciesMapper;
    @Autowired
    private SubmitConditionMapper submitConditionMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultVO<Void> insertTaxPreference(TaxPreferenceDTO taxPreferenceDTO) {
        //新增-税收优惠表t_tax_preference
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(taxPreferenceDTO);
        taxPreferenceDO.setCreateTime(new Date());
        taxPreferenceMapper.insert(taxPreferenceDO);

        //新增-税收优惠政策法规关联表t_tax_preference_ policies
        TaxPreferencePoliciesDO preferencePoliciesDO = getTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
        taxPreferencePoliciesMapper.insert(preferencePoliciesDO);

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
        TaxPreferencePoliciesDO preferencePoliciesDO = getTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
        taxPreferencePoliciesMapper.updateByTaxPreferenceId(preferencePoliciesDO);

        //修改-申报条件表 t_submit_condition
        updateSubmitConditionByTaxPreferenceId(taxPreferenceDTO);
        return ResultVO.ok();
    }

    /**
     * 修改税收优惠申报条件
     * @param taxPreferenceDTO
     * */
    private void updateSubmitConditionByTaxPreferenceId(TaxPreferenceDTO taxPreferenceDTO) {
        //采取先删除后添加的方式
        HashMap<String, Object> columnMap = new HashMap<>(16);
        columnMap.put("taxPreferenceId",taxPreferenceDTO.getId());
        taxPreferencePoliciesMapper.deleteByMap(columnMap);
        TaxPreferenceDO taxPreferenceDO = getPreferenceDO(taxPreferenceDTO);
        insertSubmitConditionDOs(taxPreferenceDTO,taxPreferenceDO);
    }

    private TaxPreferenceDO getPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        TaxPreferenceDO taxPreferenceDO = new TaxPreferenceDO();
        taxPreferenceDO.setId(taxPreferenceDTO.getId());
        return taxPreferenceDO;
    }

    /**
     * 新增到申报条件表t_submit_condition
     * */
    public void insertSubmitConditionDOs(TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        List<SubmitConditionDTO> submitConditionDTOList = taxPreferenceDTO.getSubmitConditionDTOList();
        for (int i=1;i< submitConditionDTOList.size();i++) {
            SubmitConditionDO submitConditionDO=new SubmitConditionDO();
            BeanUtils.copyProperties(submitConditionDTOList.get(i),submitConditionDO);
            submitConditionDO.setTaxPreferenceId(taxPreferenceDO.getId());
            submitConditionDO.setSort((long) i);
            submitConditionMapper.insert(submitConditionDO);
        }

    }

    /**
     * 填充TaxPreferencePoliciesDO对象
     * */
    private TaxPreferencePoliciesDO getTaxPreferencePoliciesDO(TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        TaxPreferencePoliciesDO preferencePoliciesDO=new TaxPreferencePoliciesDO();
        BeanUtils.copyProperties(taxPreferenceDTO,preferencePoliciesDO);
        preferencePoliciesDO.setTaxPreferenceId(taxPreferenceDO.getId());
        preferencePoliciesDO.setSort(1L);
        return preferencePoliciesDO;
    }

    /**
    * 填充taxPreferenceDO对象
    * */
    private TaxPreferenceDO getTaxPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        TaxPreferenceDO taxPreferenceDO=new TaxPreferenceDO();
        BeanUtils.copyProperties(taxPreferenceDTO,taxPreferenceDO);
        taxPreferenceDO.setDeleted(0);
        taxPreferenceDO.setUpdateTime(new Date());
        return taxPreferenceDO;
    }


}
