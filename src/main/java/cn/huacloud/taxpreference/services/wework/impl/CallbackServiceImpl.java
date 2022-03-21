package cn.huacloud.taxpreference.services.wework.impl;

import cn.huacloud.taxpreference.config.WeWorkConfig;
import cn.huacloud.taxpreference.services.wework.CallbackService;
import cn.huacloud.taxpreference.services.wework.crypt.MessageCrypt;
import cn.huacloud.taxpreference.services.wework.crypt.MessageCryptFactory;
import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import cn.huacloud.taxpreference.services.wework.entity.model.CallbackQuery;
import cn.huacloud.taxpreference.services.wework.handler.WeWorkMessageHandler;
import cn.huacloud.taxpreference.services.wework.support.ObjectMapperProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.function.SupplierUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wangkh
 */
@Slf4j
@Service
public class CallbackServiceImpl implements CallbackService, InitializingBean {

    private final XmlMapper xmlMapper = ObjectMapperProvider.getXmlMapper();

    @Autowired
    private List<WeWorkMessageHandler> weWorkMessageHandlers;
    @Autowired
    private WeWorkConfig weWorkConfig;

    private Map<String, Map<String, WeWorkMessageHandler>> group2Type2Handler;

    @Override
    public String verifyUrl(String appName, CallbackQuery query) throws Exception {
        AppConfig appConfig = weWorkConfig.getAppConfigByAppName(appName);
        MessageCrypt messageCrypt = MessageCryptFactory.createMessageCrypt(appConfig, appConfig.getCorpId());
        return messageCrypt.verifyUrl(query);
    }

    @Override
    public String installCallback(String appName, CallbackQuery query, String postData) throws Exception {
        return handleMessage(appName,query, postData, "安装完成回调", "InfoType");
    }

    @Override
    public String dataCallback(String appName, CallbackQuery query, String postData) throws Exception {
        return handleMessage(appName,query, postData, "数据回调", "Event");
    }

    @Override
    public String instructCallback(String appName, CallbackQuery query, String postData) throws Exception {
        return handleMessage(appName,query, postData, "指令回调", "InfoType");
    }

    private String handleMessage(String appName, CallbackQuery query, String postData, String callbackName, String groupKey) throws Exception {
        hr();
        JsonNode jsonNode = xmlMapper.readTree(postData);
        log.info("{} PostData：\n{}", callbackName, jsonNode.toPrettyString());
        // 获取消息加解密器
        String toUserName = jsonNode.get("ToUserName").asText();
        MessageCrypt messageCrypt = MessageCryptFactory.createMessageCrypt(weWorkConfig.getAppConfigByAppName(appName), toUserName);
        // 执行解密
        String decryptMsg = messageCrypt.decryptMsg(query, postData);

        JsonNode messageNode = xmlMapper.readTree(decryptMsg);
        log.info("{} Encrypt -- 解密 --> message：\n{}", callbackName, messageNode.toPrettyString());
        // 处理MsgType不是event的情况
        JsonNode msgTypeNode = messageNode.get("MsgType");
        if (msgTypeNode != null) {
            String msgType = msgTypeNode.asText();
            if (!"event".equals(msgType)) {
                log.info("{} {} MsgType -> {}", appName, callbackName, msgType);
                return SUCCESS;
            }
        }
        // 调用指定分组的消息处理器
        Map<String, WeWorkMessageHandler> handlerMap = group2Type2Handler.get(groupKey);
        if (handlerMap == null) {
            log.error("找不到对应的消息分组处理器，group：{}", groupKey);
            return SUCCESS;
        }
        if (messageNode.has(groupKey)) {
            String groupValue = messageNode.get(groupKey).asText();
            WeWorkMessageHandler messageHandler = handlerMap.get(groupValue);
            if (messageHandler == null) {
                log.error("找不到对应的消息处理器，{}：{}", groupKey, groupValue);
                return SUCCESS;
            }
            return messageHandler.handle(appName, messageNode, messageCrypt);
        } else {
            log.error("找不到对应的消息分组");
            return SUCCESS;
        }
    }

    private void hr() {
        log.info("------------------------------------------");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        group2Type2Handler = weWorkMessageHandlers.stream()
                .collect(Collectors.groupingBy(WeWorkMessageHandler::group, Collectors.toMap(WeWorkMessageHandler::type, Function.identity())));
    }
}
