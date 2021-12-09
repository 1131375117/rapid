package cn.huacloud.taxpreference.services.common.watch;

import cn.huacloud.taxpreference.services.common.entity.dos.DocStatisticsDO;
import cn.huacloud.taxpreference.sync.es.trigger.EventTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuhua
 **/
@Service
public class WatchSubject implements Subject<Long, Object> {

    @Autowired
    ApplicationContext applicationContext;
    private final List<EventTrigger<Long, Object>> eventTriggerList = new ArrayList<>();

    @Override
    public void attach(EventTrigger<Long, Object> trigger) {
        eventTriggerList.add(trigger);
    }

    @Override
    public void detach(EventTrigger<Long, Object> trigger) {
        eventTriggerList.remove(trigger);
    }

    @Override
    public void notifyChanged(DocStatisticsDO docStatisticsDO) {
        for (EventTrigger<Long, Object> eventTrigger : eventTriggerList) {
            eventTrigger.saveEvent(docStatisticsDO.getDocId());
        }
    }

}
