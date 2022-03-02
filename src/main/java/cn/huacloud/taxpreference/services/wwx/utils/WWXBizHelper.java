package cn.huacloud.taxpreference.services.wwx.utils;

import cn.huacloud.taxpreference.services.wwx.constants.WWXUrlConstants;
import cn.huacloud.taxpreference.services.wwx.entity.dos.WWXThirdCompanyDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Slf4j
public class WWXBizHelper {

    static ObjectMapper objectMapper = RESTUtil.objectMapper;

    private static void logResult(JsonNode jsonNode) {
        log.debug("\n{}", jsonNode.toPrettyString());
    }

    private static String getString(JsonNode parent, String fieldName) {
        if (parent.has(fieldName)) {
            return parent.get(fieldName).asText();
        }
        return null;
    }

    private static Integer getInteger(JsonNode parent, String fieldName) {
        if (parent.has(fieldName)) {
            return parent.get(fieldName).asInt();
        }
        return null;
    }

    public static JsonNode getPermanentCode(JsonNode createAuthInfo) {
        JsonNode authCode = createAuthInfo.get("AuthCode");
        ObjectNode body = RESTUtil.createBody();
        body.set("auth_code", authCode);
        ObjectNode data = RESTUtil.post(WWXUrlConstants.permanentCodeUrl, body);
        logResult(data);
        return data;
    }



    public static WWXThirdCompanyDO permanentCodeMap2Company(JsonNode root) throws JsonProcessingException {
        WWXThirdCompanyDO companyDO = new WWXThirdCompanyDO();
        JsonNode auth_corp_info = root.get("auth_corp_info");

        companyDO.setCorpId(getString(auth_corp_info, "corpid"));
        companyDO.setPermanentCode(getString(root, "permanent_code"));
        companyDO.setCorpName(getString(auth_corp_info, ""));
        companyDO.setStatus(1);
        companyDO.setCreateTime(LocalDateTime.now());
        return companyDO;
    }
}
