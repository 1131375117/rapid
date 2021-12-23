package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.services.consumer.entity.ess.PoliciesES;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
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

import java.util.List;
import java.util.function.Supplier;

/**
 * 政策法规ES数据事件触发器
 *
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class PoliciesEventTrigger extends EventTrigger<Long, PoliciesES> {

    private final PoliciesMapper policiesMapper;

    private final SysCodeService sysCodeService;

    private final DocStatisticsService statisticsService;

    private final PoliciesExplainEventTrigger policiesExplainEventTrigger;

    private final PoliciesExplainMapper policiesExplainMapper;

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
    public void saveEvent(Long id) {
        PoliciesES entity = getEntityById(id);
        if (entity != null) {
            saveMany.tryEmitNext(entity);
        }
        // 更新关联的政策解读
        LambdaQueryWrapper<PoliciesExplainDO> queryWrapper = Wrappers.lambdaQuery(PoliciesExplainDO.class)
                .eq(PoliciesExplainDO::getPoliciesId, id)
                .eq(PoliciesExplainDO::getDeleted, false);
        List<PoliciesExplainDO> policiesExplainDOS = policiesExplainMapper.selectList(queryWrapper);
        for (PoliciesExplainDO policiesExplainDO : policiesExplainDOS) {
            policiesExplainEventTrigger.saveEvent(policiesExplainDO.getId());
        }
    }

    @Override
    protected PoliciesES getEntityById(Long id) {
        DocStatisticsDO docStatisticsDO = statisticsService.selectDocStatistics(id, docType());
        PoliciesDO policiesDO = policiesMapper.selectById(id);
        if (policiesDO.getDeleted()) {
            return null;
        }

        // 属性拷贝
        PoliciesES policiesES = CustomBeanUtil.copyProperties(policiesDO, PoliciesES.class);
        if (policiesES == null) {
            policiesES = new PoliciesES();
        }
        if (docStatisticsDO != null) {
            policiesES.setCollections(docStatisticsDO.getCollections());
            policiesES.setViews(docStatisticsDO.getViews());
        } else {
            policiesES.setCollections(0L);
            policiesES.setViews(0L);
        }

        // 类型转换属性设置
        policiesES.setArea(sysCodeService.getSimpleVOByCode(SysCodeType.AREA,policiesDO.getAreaCode()));
        policiesES.setTaxCategories(sysCodeService.getSimpleVOListByCodeValues(SysCodeType.TAX_CATEGORIES,policiesDO.getTaxCategoriesCodes()));
        policiesES.setTaxpayerIdentifyTypes(sysCodeService.getSimpleVOListByCodeValues(SysCodeType.TAXPAYER_IDENTIFY_TYPE,policiesDO.getTaxpayerIdentifyTypeCodes()));
        policiesES.setEnterpriseTypes(sysCodeService.getSimpleVOListByCodeValues(SysCodeType.ENTERPRISE_TYPE,policiesDO.getEnterpriseTypeCodes()));
        policiesES.setIndustries(sysCodeService.getSimpleVOListByCodeValues(SysCodeType.INDUSTRY,policiesDO.getIndustryCodes()));
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
