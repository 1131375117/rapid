package cn.huacloud.taxpreference.sync.es.trigger.impl;

import cn.huacloud.taxpreference.common.enums.DocDetailsType;
import cn.huacloud.taxpreference.common.utils.CustomBeanUtil;
import cn.huacloud.taxpreference.services.consumer.entity.ess.OtherDocES;
import cn.huacloud.taxpreference.services.producer.entity.dos.OtherDocDO;
import cn.huacloud.taxpreference.services.producer.mapper.OtherDocMapper;
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
 * 税收优惠ES数据事件触发器
 * @author fuhua
 */
@RequiredArgsConstructor
@Component
public class OtherDocEventTrigger extends EventTrigger<Long, OtherDocES> {

    private final OtherDocMapper otherDocMapper;


    @Bean
    public Supplier<Flux<OtherDocES>> saveOtherDocSuppler() {
        return saveMany::asFlux;
    }

    @Bean
    public Supplier<Flux<Long>> deleteOtherDocSuppler() {
        return deleteMany::asFlux;
    }

    @Override
    protected DocDetailsType triggerType() {
        return DocDetailsType.OTHER_DOC;
    }

    @Override
    protected OtherDocES getEntityById(Long id) {
        OtherDocDO otherDocDO = otherDocMapper.selectById(id);
        // 属性拷贝
        OtherDocES otherDocES = CustomBeanUtil.copyProperties(otherDocDO, OtherDocES.class);
        otherDocES.setDocType(otherDocDO.getDocType().getSysCode());
        return otherDocES;
    }

    @Override
    protected IPage<Long> pageIdList(int pageNum, int pageSize) {
        LambdaQueryWrapper<OtherDocDO> queryWrapper = Wrappers.lambdaQuery(OtherDocDO.class);
        IPage<OtherDocDO> page = otherDocMapper.selectPage(Page.of(pageNum, pageSize), queryWrapper);
        return mapToIdPage(page, OtherDocDO::getId);
    }
}
