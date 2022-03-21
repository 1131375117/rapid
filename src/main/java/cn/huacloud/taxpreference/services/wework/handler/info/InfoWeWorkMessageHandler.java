package cn.huacloud.taxpreference.services.wework.handler.info;

import cn.huacloud.taxpreference.services.wework.handler.WeWorkMessageHandler;

/**
 * 指令回调处理
 * @author wangkh
 */
public interface InfoWeWorkMessageHandler extends WeWorkMessageHandler {

    @Override
    default String group() {
        return "InfoType";
    }
}
