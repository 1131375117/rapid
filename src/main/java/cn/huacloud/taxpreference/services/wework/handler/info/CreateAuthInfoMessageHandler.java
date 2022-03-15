package cn.huacloud.taxpreference.services.wework.handler.info;

import cn.huacloud.taxpreference.config.WeWorkConfig;
import cn.huacloud.taxpreference.services.wework.WeWorkCompanyService;
import cn.huacloud.taxpreference.services.wework.WeWorkTokenService;
import cn.huacloud.taxpreference.services.wework.client.WeWorkServiceClient;
import cn.huacloud.taxpreference.services.wework.client.entity.PermanentCode;
import cn.huacloud.taxpreference.services.wework.crypt.MessageCrypt;
import cn.huacloud.taxpreference.services.wework.entity.dos.WeWorkCompanyDO;
import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangkh
 */
@Component
public class CreateAuthInfoMessageHandler implements InfoWeWorkMessageHandler {

    @Autowired
    private WeWorkServiceClient serviceClient;
    @Autowired
    private WeWorkTokenService weWorkTokenService;
    @Autowired
    private WeWorkCompanyService weWorkCompanyService;
    @Autowired
    private WeWorkConfig weWorkConfig;

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
        WeWorkCompanyDO weWorkCompanyDO = map2Company(permanentCode);
        // 这个agentId不知道是什么，直接用suiteId替换了
        AppConfig appConfig = weWorkConfig.getAppConfigByAppName(appName);
        weWorkCompanyDO.setSuiteId(appConfig.getSuiteId());

        weWorkCompanyService.save(weWorkCompanyDO);
        return SUCCESS;
    }

    private static WeWorkCompanyDO map2Company(PermanentCode permanentCode) {
        WeWorkCompanyDO companyDO = new WeWorkCompanyDO();
        companyDO.setPermanentCode(permanentCode.getPermanent_code());
        // 公司信息
        PermanentCode.auth_corp_info auth_corp_info = permanentCode.getAuth_corp_info();
        if (auth_corp_info != null) {
            companyDO.setCorpId(auth_corp_info.getCorpid())
                    .setCorpName(auth_corp_info.getCorp_name())
                    .setCorpFullName(auth_corp_info.getCorp_full_name())
                    .setSubjectType(auth_corp_info.getSubject_type())
                    .setVerifiedEndTime(auth_corp_info.getVerified_end_time());
        }
        PermanentCode.auth_info auth_info = permanentCode.getAuth_info();
        // 这个agentId不知道是什么，直接用suiteId替换了
        /*if (auth_corp_info != null) {
            List<PermanentCode.agent> agents = auth_info.getAgent();
            if (!CollectionUtils.isEmpty(agents)) {
                PermanentCode.agent agent = agents.get(0);
                companyDO.setAgentId(agent.getAgentid());
            }
        }*/
        return companyDO;
    }
}
