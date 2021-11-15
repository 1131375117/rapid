package cn.huacloud.taxpreference.sync.es.trigger;

import cn.huacloud.taxpreference.common.enums.SysCodeGetter;
import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import reactor.core.publisher.Sinks;

import java.util.function.Function;

/**
 * 事件触发器
 *
 * @author wangkh
 */
public abstract class EventTrigger<T, R> {

    protected Sinks.Many<R> saveMany = Sinks.many().unicast().onBackpressureBuffer();

    protected Sinks.Many<T> deleteMany = Sinks.many().unicast().onBackpressureBuffer();

    public void saveEvent(T id) {
        R entity = getEntityById(id);
        if (entity != null) {
            saveMany.tryEmitNext(entity);
        }
    }

    public void deleteEvent(T id) {
        deleteMany.tryEmitNext(id);
    }

    protected abstract R getEntityById(T id);

    public void syncAll() {

    }

    public SysCodeSimpleVO getEnumSysCode(SysCodeGetter sysCodeGetter) {
        if (sysCodeGetter == null) {
            return new SysCodeSimpleVO();
        }
        return sysCodeGetter.getSysCode();
    }
}
