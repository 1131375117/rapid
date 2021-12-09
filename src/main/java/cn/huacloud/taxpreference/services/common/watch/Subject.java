package cn.huacloud.taxpreference.services.common.watch;


import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;

/**
 * @author hua-cloud
 */
public interface Subject<T, R> {


    /**
     * 订阅操作
     */
    void attach(EventTrigger<T, R> trigger) ;

    /**
     * 取消订阅操作
     */
    void detach(EventTrigger<T, R> trigger);

    /**
     * 通知变动
     */
    void notifyChanged(DocStatisticsDO docStatisticsDO);

}
