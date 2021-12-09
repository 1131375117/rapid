package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.services.consumer.entity.ess.PoliciesES;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

/**
 * 政策法规ES数据事件触发器
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class PoliciesEventTrigger extends EventTrigger<Long, PoliciesES> {

    private final PoliciesMapper policiesMapper;

    private final SysCodeService sysCodeService;

    private final DocStatisticsService statisticsService;

    @Bean
    public Supplier<Flux<PoliciesES>> savePoliciesSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<Long>> deletePoliciesSuppler() {
        return deleteMany::asFlux;
    }

    @Override
    public DocType docType() {
        return DocType.POLICIES;
    }

    @Override
    protected PoliciesES getEntityById(Long id) {
        DocStatisticsDO docStatisticsDO = statisticsService.selectOne(id, docType());
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        if (policiesDO.getDeleted()) {
            return null;
        }

        // 属性拷贝
        PoliciesES policiesES = CustomBeanUtil.copyProperties(policiesDO, PoliciesES.class);
        CustomBeanUtil.copyProperties(docStatisticsDO, PoliciesES.class);

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
    protected IPage<Long> pageIdList(int pageNum, int pageSize) {
        LambdaQueryWrapper<PoliciesDO> queryWrapper = Wrappers.lambdaQuery(PoliciesDO.class)
                .eq(PoliciesDO::getDeleted, false);
        IPage<PoliciesDO> page = policiesMapper.selectPage(Page.of(pageNum, pageSize), queryWrapper);
        return mapToIdPage(page, PoliciesDO::getId);
    }
}
