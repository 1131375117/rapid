package cn.huacloud.taxpreference.sync.es.consumer;

import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.openapi.MonitorService;
import cn.huacloud.taxpreference.services.openapi.mapper.ApiMonitorInfoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

/**
 * @author fuhua
 */
public interface MySqlConsumer<T> {

    default MonitorService getMonitorService() {
        return SpringUtil.getBean(MonitorService.class);
    }

    default ApiMonitorInfoMapper getUserMonitorInfoMapper() {
        return SpringUtil.getBean(ApiMonitorInfoMapper.class);
    }

    Logger getLog();

    default ObjectMapper getObjectMapper() {
        return SpringUtil.getBean(ObjectMapper.class);
    }

    default void save(T source) {
    }

    default void update(T source) {

    }


}
