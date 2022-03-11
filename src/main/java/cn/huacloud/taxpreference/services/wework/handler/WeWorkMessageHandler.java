package cn.huacloud.taxpreference.services.wework.handler;

import cn.huacloud.taxpreference.services.wework.crypt.MessageCrypt;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * 消息处理器
 * @author wangkh
 */
public interface WeWorkMessageHandler {

    String SUCCESS = "success";
    String FAIL = "fail";

    // 分组信息
    String group();
    // 类型信息
    String type();
    // 消息处理
    String handle(String appName, JsonNode message, MessageCrypt messageCrypt);
}
