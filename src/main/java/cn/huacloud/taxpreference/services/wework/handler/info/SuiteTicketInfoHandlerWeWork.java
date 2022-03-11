package cn.huacloud.taxpreference.services.wework.handler.info;

import cn.huacloud.taxpreference.services.wework.WeWorkTokenService;
import cn.huacloud.taxpreference.services.wework.crypt.MessageCrypt;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangkh
 */
@Component
public class SuiteTicketInfoHandlerWeWork implements InfoWeWorkMessageHandler {

    @Autowired
    private WeWorkTokenService weWorkTokenService;

    @Override
    public String type() {
        return "suite_ticket";
    }

    @Override
    public String handle(String appName, JsonNode message, MessageCrypt messageCrypt) {
        String suiteTicket = message.get("SuiteTicket").asText();
        weWorkTokenService.setSuiteTicket(appName, suiteTicket);
        return SUCCESS;
    }
}
