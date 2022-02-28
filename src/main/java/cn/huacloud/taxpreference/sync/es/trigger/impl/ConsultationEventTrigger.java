package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.common.DocStatisticsService;
import cn.huacloud.taxpreference.services.common.SysCodeService;
import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.services.consumer.entity.ess.ConsultationES;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationContentESVO;
import cn.huacloud.taxpreference.services.producer.entity.dos.ConsultationContentDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.ConsultationDO;
import cn.huacloud.taxpreference.services.producer.mapper.ConsultationContentMapper;
import cn.huacloud.taxpreference.services.producer.mapper.ConsultationMapper;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * 税收优惠ES数据事件触发器
 *
 * @author fuhua
 */
@RequiredArgsConstructor
@Component
public class ConsultationEventTrigger extends EventTrigger<Long, ConsultationES> {

    private final ConsultationMapper consultationMapper;
    private final ConsultationContentMapper contentMapper;
    private final DocStatisticsService docStatisticsService;
    private final SysCodeService sysCodeService;


    @Bean
    public Supplier<Flux<ConsultationES>> saveConsultationSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<Long>> deleteConsultationSuppler() {
        return deleteMany::asFlux;
    }

    @Override
    public DocType docType() {
        return DocType.CONSULTATION;
    }

    @Override
    protected ConsultationES getEntityById(Long id) {
        //获取概要信息
        ConsultationDO consultationDO = consultationMapper.selectById(id);


        //获取content内容信息
        LambdaQueryWrapper<ConsultationContentDO> queryWrapper = Wrappers.lambdaQuery(ConsultationContentDO.class).eq(ConsultationContentDO::getConsultationId, id);
        List<ConsultationContentDO> consultationContentDOList = contentMapper.selectList(queryWrapper);
        //获取收藏浏览
        DocStatisticsDO docStatisticsDO = docStatisticsService.selectDocStatistics(id, docType());

        // 属性拷贝
        ConsultationES consultationES = CustomBeanUtil.copyProperties(consultationDO, ConsultationES.class);

        consultationES.setTaxCategories(sysCodeService.getSimpleVOListByCodeValues(SysCodeType.TAX_CATEGORIES, consultationDO.getTaxCategoriesCodes()));
        consultationES.setIndustries(sysCodeService.getSimpleVOListByCodeValues(SysCodeType.INDUSTRY, consultationDO.getIndustryCodes()));

        if (consultationES == null) {
            consultationES = new ConsultationES();
        }
        if (docStatisticsDO != null) {
            consultationES.setCollections(docStatisticsDO.getCollections());
            consultationES.setViews(docStatisticsDO.getViews());
        } else {
            consultationES.setCollections(0L);
            consultationES.setViews(0L);
        }
        //copy子表内容
        List<ConsultationContentESVO> consultationContentESVOList = new ArrayList<>();
        for (ConsultationContentDO consultationContentDO : consultationContentDOList) {
            ConsultationContentESVO consultationContentESVO = new ConsultationContentESVO();
            CustomBeanUtil.copyProperties(consultationContentDO, consultationContentESVO);
            if(!StringUtils.isEmpty(consultationContentDO.getImageUris())){
                consultationContentESVO.setImageUris(Arrays.asList(consultationContentDO.getImageUris().split(",")));
            }else{
                consultationContentESVO.setImageUris(new ArrayList<>());
            }
            consultationContentESVOList.add(consultationContentESVO);
        }
        consultationES.setConsultationContent(consultationContentESVOList);

        return consultationES;
    }

    @Override
    protected IPage<Long> pageIdList(int pageNum, int pageSize) {
        return null;
    }


}
