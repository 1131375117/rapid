package cn.huacloud.taxpreference.services.openapi.message;

import cn.huacloud.taxpreference.services.openapi.entity.dos.ApiMonitorInfoDO;

/**
 * OpenAPI监控记录消息发送器
 * 为了不影响接口调用，接口调用记录采用消息异步
 * @author wangkh
 */
public interface OpenApiMessageSender {

    /**
     * 发送API监控记录
     * @param apiMonitorInfoDO
     */
    void sendApiMonitorInfo(ApiMonitorInfoDO apiMonitorInfoDO);
}
