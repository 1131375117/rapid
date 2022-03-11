package cn.huacloud.taxpreference.services.wework.handler.event;

import cn.huacloud.taxpreference.services.wework.crypt.MessageCrypt;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * @author wangkh
 */
@Component
public class ChangeAppAdminEventWeWorkMessageHandler implements EventWeWorkMessageHandler {
    @Override
    public String type() {
        return "change_app_admin";
    }

    @Override
    public String handle(String appName, JsonNode message, MessageCrypt messageCrypt) {
        return SUCCESS;
    }
}
