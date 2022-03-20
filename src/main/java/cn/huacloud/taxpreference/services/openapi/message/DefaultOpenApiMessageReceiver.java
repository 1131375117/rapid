package cn.huacloud.taxpreference.services.openapi.message;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiMonitorInfoDO;
import cn.huacloud.taxpreference.services.openapi.mapper.ApiMonitorInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 默认消息接收器
 * <p/> 默认接收消息保存到mysql数据库
 * @author wangkh
 */
@Configuration
public class DefaultOpenApiMessageReceiver {

    private ApiMonitorInfoMapper apiMonitorInfoMapper;

    @Autowired
    public void setApiMonitorInfoMapper(ApiMonitorInfoMapper apiMonitorInfoMapper) {
        this.apiMonitorInfoMapper = apiMonitorInfoMapper;
    }

    @Bean("apiMonitorInfoConsumer")
    public Consumer<ApiMonitorInfoDO> apiMonitorInfoConsumer() {
        // 直接把API调用记录保存到数据库中
        return apiMonitorInfoMapper::insert;
    }
}
