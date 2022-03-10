package cn.huacloud.taxpreference.services.wework.handler.info;

import cn.huacloud.taxpreference.services.wework.WeWorkTokenService;
import cn.huacloud.taxpreference.services.wework.client.WeWorkServiceClient;
import cn.huacloud.taxpreference.services.wework.client.entity.PermanentCode;
import cn.huacloud.taxpreference.services.wework.crypt.MessageCrypt;
import cn.huacloud.taxpreference.services.wework.support.ObjectMapperProvider;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangkh
 */
@Component
public class CreateAuthInfoHandlerWeWork implements InfoWeWorkMessageHandler {

    @Autowired
    private WeWorkServiceClient serviceClient;
    @Autowired
    private WeWorkTokenService weWorkTokenService;

    @Override
    public String type() {
        return "create_auth";
    }

    @Override
    public String handle(String appName, JsonNode message, MessageCrypt messageCrypt) {
        String authCode = message.get("AuthCode").asText();
        String suiteToken = weWorkTokenService.getSuiteToken(appName);
        PermanentCode.Request request = new PermanentCode.Request();
        request.setAuth_code(authCode);
        PermanentCode permanentCode = serviceClient.getPermanentCode(suiteToken, request);
        String json = ObjectMapperProvider.writeJsonPrettyString(permanentCode);
        System.out.println(json);
        return SUCCESS;
    }
}
