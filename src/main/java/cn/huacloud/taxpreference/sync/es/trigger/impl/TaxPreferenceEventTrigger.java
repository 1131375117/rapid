package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.consumer.entity.ess.TaxPreferenceES;
import cn.huacloud.taxpreference.services.consumer.entity.vos.PoliciesDigestSearchVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.SubmitConditionSearchVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.mapper.SubmitConditionMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferenceMapper;
import cn.huacloud.taxpreference.services.producer.mapper.TaxPreferencePoliciesMapper;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 税收优惠ES数据事件触发器
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class TaxPreferenceEventTrigger extends EventTrigger<Long, TaxPreferenceES> {

    private final TaxPreferenceMapper taxPreferenceMapper;

    private final SubmitConditionMapper submitConditionMapper;

    private final TaxPreferencePoliciesMapper taxPreferencePoliciesMapper;

    private final SysCodeService sysCodeService;

    @Bean
    public Supplier<Flux<TaxPreferenceES>> saveTaxPreferenceSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<Long>> deleteTaxPreferenceSuppler() {
        return deleteMany::asFlux;
    }

    @Override
    protected TaxPreferenceES getEntityById(Long id) {
        TaxPreferenceDO taxPreferenceDO = taxPreferenceMapper.selectById(id);
        if (taxPreferenceDO.getDeleted()) {
            return null;
        }

        // 属性拷贝
        TaxPreferenceES taxPreferenceES = CustomBeanUtil.copyProperties(taxPreferenceDO, TaxPreferenceES.class);

        // 类型转换属性设置
        taxPreferenceES.setTitle(taxPreferenceDO.getTaxPreferenceName());
        taxPreferenceES.setTaxCategories(sysCodeService.getSimpleVOByCode(taxPreferenceDO.getTaxCategoriesCode()));
        taxPreferenceES.setTaxpayerRegisterType(sysCodeService.getSimpleVOByCode(taxPreferenceDO.getTaxpayerRegisterTypeCode()));
        taxPreferenceES.setTaxpayerType(sysCodeService.getSimpleVOByCode(taxPreferenceDO.getTaxpayerTypeCode()));
        taxPreferenceES.setIndustries(sysCodeService.getSimpleVOListByCodeValues(taxPreferenceDO.getIndustryCodes()));
        taxPreferenceES.setEnterpriseTypes(sysCodeService.getSimpleVOListByCodeValues(taxPreferenceDO.getEnterpriseTypeCodes()));
        taxPreferenceES.setValidity(getEnumSysCode(taxPreferenceDO.getValidity()));
        taxPreferenceES.setLabels(split2List(taxPreferenceDO.getLabels()));

        // 设置政策
        List<PoliciesDigestSearchVO> policiesDigestSearchVOList = taxPreferencePoliciesMapper.getPoliciesDigestSearchVOList(taxPreferenceDO.getId());
        taxPreferenceES.setPolicies(policiesDigestSearchVOList);

        // 设置申报条件
        List<SubmitConditionSearchVO> submitConditions = submitConditionMapper.getSubmitConditions(taxPreferenceDO.getId()).stream()
                .map(submitConditionDO -> CustomBeanUtil.copyProperties(submitConditionDO, SubmitConditionSearchVO.class))
                .collect(Collectors.toList());
        taxPreferenceES.setSubmitConditions(submitConditions);

        return taxPreferenceES;
    }

    @Override
    protected IPage<Long> pageIdList(int pageNum, int pageSize) {
        LambdaQueryWrapper<TaxPreferenceDO> queryWrapper = Wrappers.lambdaQuery(TaxPreferenceDO.class)
                .eq(TaxPreferenceDO::getDeleted, false);
        IPage<TaxPreferenceDO> page = taxPreferenceMapper.selectPage(Page.of(pageNum, pageSize), queryWrapper);
        return mapToIdPage(page, TaxPreferenceDO::getId);
    }
}
