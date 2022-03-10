package cn.huacloud.taxpreference.services.wework.handler.event;

import cn.huacloud.taxpreference.services.wework.handler.WeWorkMessageHandler;

/**
 * @author wangkh
 */
public interface EventWeWorkMessageHandler extends WeWorkMessageHandler {
    @Override
    default String group() {
        return "Event";
    }
}
