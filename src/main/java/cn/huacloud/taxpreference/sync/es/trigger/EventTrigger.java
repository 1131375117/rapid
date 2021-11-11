package cn.huacloud.taxpreference.sync.es.trigger;

import reactor.core.publisher.Sinks;

import java.util.List;
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
        saveMany.tryEmitNext(queryFunction().apply(id));
    }

    public void deleteEvent(T id) {
        deleteMany.tryEmitNext(id);
    }

    protected abstract Function<T, R> queryFunction();
}
