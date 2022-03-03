package cn.huacloud.taxpreference.services.message.handler;

import java.util.List;

/**
 * 短信具体业务发送处理器
 *
 * @author fuhua
 **/
public interface EmailBizHandler {


        /**
         * 前置处理
         */
        default void beforeHandle(List<String> emails) {

        }

        /**
         * 获取参数
         */
        List<String> getParams(List<String> emails);

        /**
         * 后置处理
         */
        default void afterHandle(List<String> emails) {

        }

}
