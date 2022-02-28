/*
package cn.huacloud.taxpreference.sync.es.consumer;

import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.common.MonitorService;
import cn.huacloud.taxpreference.services.common.entity.dos.ApiUserStatisticsDO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

*/
/**
 * @author fuhua
 *//*

public interface MySqlConsumer<T> {

    default MonitorService getMonitorService() {
        return SpringUtil.getBean(MonitorService.class);
    }

    Logger getLog();

    default ObjectMapper getObjectMapper() {
        return SpringUtil.getBean(ObjectMapper.class);
    }

    default void save(T source) {
        ApiUserStatisticsDO apiUserStatisticsDO = (ApiUserStatisticsDO) source;
        getMonitorService().insert(apiUserStatisticsDO);
    }

    default void update(T source) {
        ApiUserStatisticsDO apiUserStatisticsDO = (ApiUserStatisticsDO) source;
        getMonitorService().update(apiUserStatisticsDO);
    }


}
*/
