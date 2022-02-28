/*
package cn.huacloud.taxpreference.sync.es.trigger;

import reactor.core.publisher.Sinks;

*/
/**
 * 事件触发器
 *
 * @author wangkh
 *//*

public abstract class MysqlEventTrigger<T> {

    protected Sinks.Many<T> saveMany = Sinks.many().unicast().onBackpressureBuffer();

    protected Sinks.Many<T> updateMany = Sinks.many().unicast().onBackpressureBuffer();

    public void saveEvent(T t) {
        if (t != null) {
            saveMany.tryEmitNext(t);
        }
    }

    */
/**
     * 触发删除事件
     *
     * @param t 主键ID
     *//*

    public void updateEvent(T t) {
        updateMany.tryEmitNext(t);
    }

}
*/
