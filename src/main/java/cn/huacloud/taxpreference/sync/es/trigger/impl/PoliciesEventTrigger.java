package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.consumer.entity.ess.PoliciesES;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class PoliciesEventTrigger extends EventTrigger<Long, PoliciesES> {

    private final PoliciesMapper policiesMapper;

    private final SysCodeService sysCodeService;

    @Bean
    public Supplier<Flux<PoliciesES>> savePoliciesSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<Long>> deletePoliciesSuppler() {
        return deleteMany::asFlux;
    }

    @Override
    protected PoliciesES getEntityById(Long id) {
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        if (policiesDO.getDeleted()) {
            return null;
        }

        // 属性拷贝
        PoliciesES policiesES = new PoliciesES();
        BeanUtils.copyProperties(policiesDO, policiesES);

        // 类型转换属性设置
        policiesES.setArea(sysCodeService.getSimpleVOByCode(policiesDO.getAreaCode()));
        policiesES.setTaxCategories(sysCodeService.getSimpleVOByCode(policiesDO.getTaxCategoriesCode()));
        policiesES.setTaxpayerIdentifyTypes(sysCodeService.getSimpleVOListByCodeValues(policiesDO.getTaxpayerIdentifyTypeCodes()));
        policiesES.setEnterpriseTypes(sysCodeService.getSimpleVOListByCodeValues(policiesDO.getEnterpriseTypeCodes()));
        policiesES.setIndustries(sysCodeService.getSimpleVOListByCodeValues(policiesDO.getIndustryCodes()));
        policiesES.setValidity(getEnumSysCode(policiesDO.getValidity()));
        policiesES.setLabels(split2List(policiesDO.getLabels()));

        return policiesES;
    }

    @Override
    protected IPage<Long> pageIdList(IPage<?> queryPage) {
        LambdaQueryWrapper<PoliciesDO> queryWrapper = Wrappers.lambdaQuery(PoliciesDO.class)
                .eq(PoliciesDO::getDeleted, false);
        IPage<PoliciesDO> page = policiesMapper.selectPage((IPage<PoliciesDO>) queryPage, queryWrapper);
        List<Long> ids = page.getRecords().stream()
                .map(PoliciesDO::getId)
                .collect(Collectors.toList());
        Page<Long> idPage = new Page<>(queryPage.getCurrent(), queryPage.getSize(), queryPage.getTotal());
        idPage.setRecords(ids);
        return idPage;
    }

    private List<String> split2List(String value) {
        if (StringUtils.isBlank(value)) {
            return new ArrayList<>();
        }
        return Arrays.asList(value.split(","));
    }
}
