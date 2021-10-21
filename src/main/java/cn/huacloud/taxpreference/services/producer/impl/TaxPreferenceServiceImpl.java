package cn.huacloud.taxpreference.services.producer.impl;

import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.producer.TaxPreferenceService;
import cn.huacloud.taxpreference.services.producer.entity.dos.SubmitConditionDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferencePoliciesDO;
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
        taxPreferenceMapper.insert(taxPreferenceDO);

        //新增-税收优惠政策法规关联表t_tax_preference_ policies
        TaxPreferencePoliciesDO preferencePoliciesDO = getTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
        taxPreferencePoliciesMapper.insert(preferencePoliciesDO);

        //新增-申报条件表 t_submit_condition
        SubmitConditionDO submitConditionDO = getSubmitConditionDO(taxPreferenceDTO, taxPreferenceDO);
        submitConditionMapper.insert(submitConditionDO);
        return ResultVO.ok();
    }

    @Override
    public ResultVO<Void> updateTaxPreference(TaxPreferenceDTO taxPreferenceDTO) {
        //修改-税收优惠表t_tax_preference
        TaxPreferenceDO taxPreferenceDO = getTaxPreferenceDO(taxPreferenceDTO);
        taxPreferenceMapper.updateById(taxPreferenceDO);

        //修改-税收优惠政策法规关联表t_tax_preference_ policies
        TaxPreferencePoliciesDO preferencePoliciesDO = getTaxPreferencePoliciesDO(taxPreferenceDTO, taxPreferenceDO);
        taxPreferencePoliciesMapper.insert(preferencePoliciesDO);

        //修改-申报条件表 t_submit_condition
        SubmitConditionDO submitConditionDO = getSubmitConditionDO(taxPreferenceDTO, taxPreferenceDO);
        submitConditionMapper.insert(submitConditionDO);
        return null;
    }

    private SubmitConditionDO getSubmitConditionDO(TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        SubmitConditionDO submitConditionDO=new SubmitConditionDO();
        BeanUtils.copyProperties(taxPreferenceDTO,submitConditionDO);
        submitConditionDO.setTaxPreferenceId(taxPreferenceDO.getId());
        submitConditionDO.setSort(1L);
        return submitConditionDO;
    }

    private TaxPreferencePoliciesDO getTaxPreferencePoliciesDO(TaxPreferenceDTO taxPreferenceDTO, TaxPreferenceDO taxPreferenceDO) {
        TaxPreferencePoliciesDO preferencePoliciesDO=new TaxPreferencePoliciesDO();
        BeanUtils.copyProperties(taxPreferenceDTO,preferencePoliciesDO);
        preferencePoliciesDO.setTaxPreferenceId(taxPreferenceDO.getId());
        preferencePoliciesDO.setSort(1L);
        return preferencePoliciesDO;
    }

    private TaxPreferenceDO getTaxPreferenceDO(TaxPreferenceDTO taxPreferenceDTO) {
        TaxPreferenceDO taxPreferenceDO=new TaxPreferenceDO();
        BeanUtils.copyProperties(taxPreferenceDTO,taxPreferenceDO);
        taxPreferenceDO.setDeleted(0);
        taxPreferenceDO.setUpdateTime(new Date());
        taxPreferenceDO.setCreateTime(new Date());
        return taxPreferenceDO;
    }


}
