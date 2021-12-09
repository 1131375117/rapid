package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.common.enums.DocDetailsType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.consumer.entity.ess.FrequentlyAskedQuestionES;
import cn.huacloud.taxpreference.services.producer.entity.dos.FrequentlyAskedQuestionDO;
import cn.huacloud.taxpreference.services.producer.mapper.FrequentlyAskedQuestionMapper;
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
 * 热点问答ES数据事件触发器
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class FAQEventTrigger extends EventTrigger<Long, FrequentlyAskedQuestionES> {

    private final FrequentlyAskedQuestionMapper frequentlyAskedQuestionMapper;

    @Bean
    public Supplier<Flux<FrequentlyAskedQuestionES>> saveFAQSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<Long>> deleteFAQSuppler() {
        return deleteMany::asFlux;
    }


    @Override
    protected DocDetailsType triggerType() {
        return DocDetailsType.FREQUENTLY_ASKED_QUESTION;
    }

    @Override
    protected FrequentlyAskedQuestionES getEntityById(Long id) {
        FrequentlyAskedQuestionDO faqDO = frequentlyAskedQuestionMapper.selectById(id);
        if (faqDO.getDeleted()) {
            return null;
        }

        // 属性拷贝
        FrequentlyAskedQuestionES faqES = CustomBeanUtil.copyProperties(faqDO, FrequentlyAskedQuestionES.class);

        // 属性转换
        faqES.setPoliciesIds(split2List(faqDO.getPoliciesIds()));

        return faqES;
    }

    @Override
    protected IPage<Long> pageIdList(int pageNum, int pageSize) {
        LambdaQueryWrapper<FrequentlyAskedQuestionDO> queryWrapper = Wrappers.lambdaQuery(FrequentlyAskedQuestionDO.class)
                .eq(FrequentlyAskedQuestionDO::getDeleted, false);
        IPage<FrequentlyAskedQuestionDO> page = frequentlyAskedQuestionMapper.selectPage(Page.of(pageNum, pageSize), queryWrapper);
        return mapToIdPage(page, FrequentlyAskedQuestionDO::getId);
    }
}
