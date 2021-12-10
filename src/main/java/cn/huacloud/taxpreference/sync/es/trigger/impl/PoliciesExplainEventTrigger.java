package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.services.consumer.entity.ess.PoliciesExplainES;
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

import java.util.function.Supplier;

/**
 * 政策解读ES数据事件触发器
 *
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class PoliciesExplainEventTrigger extends EventTrigger<Long, PoliciesExplainES> {

    private final PoliciesMapper policiesMapper;

    private final PoliciesExplainMapper policiesExplainMapper;

    private final SysCodeService sysCodeService;

    private final DocStatisticsService statisticsService;

    @Bean
    public Supplier<Flux<PoliciesExplainES>> savePoliciesExplainSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<Long>> deletePoliciesExplainSuppler() {
        return deleteMany::asFlux;
    }

    @Override
    public DocType docType() {
        return DocType.POLICIES_EXPLAIN;
    }

    @Override
    protected PoliciesExplainES getEntityById(Long id) {
    //    DocStatisticsDO docStatisticsDO = statisticsService.selectDocStatistics(id, docType());
        PoliciesExplainDO policiesExplainDO = policiesExplainMapper.selectById(id);

        if (policiesExplainDO.getDeleted()) {
            return null;
        }

        PoliciesDO policiesDO = policiesMapper.selectById(policiesExplainDO.getPoliciesId());
        if (policiesDO == null) {
            policiesDO = new PoliciesDO();
        }

        // 属性拷贝
        PoliciesExplainES policiesExplainES = CustomBeanUtil.copyProperties(policiesExplainDO, PoliciesExplainES.class);
   //     CustomBeanUtil.copyProperties(docStatisticsDO, PoliciesExplainES.class);

        // 类型转换属性设置
        policiesExplainES.setTaxCategories(sysCodeService.getSimpleVOByCode(policiesDO.getTaxCategoriesCode()));
        policiesExplainES.setArea(sysCodeService.getSimpleVOByCode(policiesDO.getAreaCode()));
        policiesExplainES.setTaxpayerIdentifyTypes(sysCodeService.getSimpleVOListByCodeValues(policiesDO.getTaxpayerIdentifyTypeCodes()));
        policiesExplainES.setEnterpriseTypes(sysCodeService.getSimpleVOListByCodeValues(policiesDO.getEnterpriseTypeCodes()));
        policiesExplainES.setIndustries(sysCodeService.getSimpleVOListByCodeValues(policiesDO.getIndustryCodes()));

        return policiesExplainES;
    }

    @Override
    protected IPage<Long> pageIdList(int pageNum, int pageSize) {
        LambdaQueryWrapper<PoliciesExplainDO> queryWrapper = Wrappers.lambdaQuery(PoliciesExplainDO.class)
                .eq(PoliciesExplainDO::getDeleted, false);
        IPage<PoliciesExplainDO> page = policiesExplainMapper.selectPage(Page.of(pageNum, pageSize), queryWrapper);
        return mapToIdPage(page, PoliciesExplainDO::getId);
    }
}
