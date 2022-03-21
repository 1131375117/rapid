package cn.huacloud.taxpreference.services.wework.handler.info;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.huacloud.taxpreference.config.WeWorkConfig;
import cn.huacloud.taxpreference.services.wework.WeWorkCompanyService;
import cn.huacloud.taxpreference.services.wework.crypt.MessageCrypt;
import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangkh
 */
@Component
public class CancelAuthInfoMessageHandler implements InfoWeWorkMessageHandler {
    @Autowired
    private WeWorkCompanyService weWorkCompanyService;

    @Override
    public String type() {
        return "cancel_auth";
    }

    @Override
    public String handle(String appName, JsonNode message, MessageCrypt messageCrypt) {
        String authCorpId = message.get("AuthCorpId").asText();
        String suiteId = message.get("SuiteId").asText();
        weWorkCompanyService.deleteByCorpIdAndAgentId(authCorpId, suiteId);
        return SUCCESS;
    }
}
