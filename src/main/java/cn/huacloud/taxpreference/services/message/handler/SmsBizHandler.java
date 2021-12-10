package cn.huacloud.taxpreference.services.message.handler;

import java.util.List;

/**
 * @author wangkh
 */
public interface SmsBizHandler {

    /**
     * 前置处理
     */
    default void beforeHandle(List<String> phoneNumbers) {

    }

    /**
     * 获取参数
     */
    List<String> getParams(List<String> phoneNumbers);

    /**
     * 后置处理
     */
    default void afterHandle(List<String> phoneNumbers) {

    }
}
