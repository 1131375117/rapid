package cn.huacloud.taxpreference.sync.es.consumer;

import cn.huacloud.taxpreference.common.utils.SpringUtil;
import cn.huacloud.taxpreference.services.common.MonitorService;
import cn.huacloud.taxpreference.services.common.mapper.UserMonitorInfoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

/**
 * @author fuhua
 */
public interface MySqlConsumer<T> {

    default MonitorService getMonitorService() {
        return SpringUtil.getBean(MonitorService.class);
    }

    default UserMonitorInfoMapper getUserMonitorInfoMapper() {
        return SpringUtil.getBean(UserMonitorInfoMapper.class);
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
