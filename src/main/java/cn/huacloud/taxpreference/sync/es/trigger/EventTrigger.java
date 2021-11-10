package cn.huacloud.taxpreference.sync.es.trigger;

/**
 * 事件触发器
 * @author wangkh
 */
public interface EventTrigger<T> {

    void saveEvent(T value);

    void deleteEvent(T value);

}
