package cn.huacloud.taxpreference.services.wwx;

import cn.huacloud.taxpreference.config.WWXConfig;
import cn.huacloud.taxpreference.services.wwx.ase.MessageHelper;
import cn.huacloud.taxpreference.services.wwx.ase.MessageHelperFactory;
import cn.huacloud.taxpreference.services.wwx.constants.ReceiveIdGetter;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackBodyDTO;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackQueryDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;

import java.util.function.Function;

/**
 * 企业微信第三方应用支持服务
 * @author wangkh
 */
public interface WWXService {

    XmlMapper xmlMapper = (XmlMapper) new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    String getAppName();

    Logger getLogger();

    WWXConfig.App getAppConfig();

    default String verifyURL(CallbackQueryDTO queryDTO) throws Exception {
        WWXConfig.App appConfig = getAppConfig();
        String receiveId = ReceiveIdGetter.VERIFY_URL.apply(appConfig);
        MessageHelper messageHelper = MessageHelperFactory.createMessageHelper(appConfig, receiveId);
        return messageHelper.verifyURL(queryDTO);
    }

    default String installCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception {
        Logger log = getLogger();
        log.info("------------------------------");
        log.info("【{}】开始注册回调", getAppName());
        log.info("bodyStr: {}", bodyStr);
        CallbackBodyDTO bodyDTO = xmlMapper.readValue(bodyStr, CallbackBodyDTO.class);
        String encrypt = bodyDTO.getEncrypt();
        // 获取消息处理器
        WWXConfig.App appConfig = getAppConfig();
        MessageHelper messageHelper = MessageHelperFactory.createMessageHelper(appConfig, ReceiveIdGetter.INSTALL_CALLBACK.apply(appConfig));
        // 签名验证
        messageHelper.verifySignature(queryDTO, encrypt);
        // 加密数据解密
        String decrypt = messageHelper.decrypt(encrypt);
        readXml2JsonNode(decrypt);
        return "success";
    }

    default String dataCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception {
        Logger log = getLogger();
        log.info("------------------------------");
        log.info("【{}】开始数据回调", getAppName());
        log.info("bodyStr: {}", bodyStr);
        CallbackBodyDTO bodyDTO = xmlMapper.readValue(bodyStr, CallbackBodyDTO.class);
        String encrypt = bodyDTO.getEncrypt();
        // 获取消息处理器
        MessageHelper messageHelper = MessageHelperFactory.createMessageHelper(getAppConfig(), ReceiveIdGetter.DATA_CALLBACK.apply(bodyDTO));
        // 签名验证
        messageHelper.verifySignature(queryDTO, encrypt);
        // 加密数据解密
        String decrypt = messageHelper.decrypt(encrypt);
        readXml2JsonNode(decrypt);
        return "success";
    }

    default String instructCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception {
        Logger log = getLogger();
        log.info("------------------------------");
        log.info("【{}】开始指令回调", getAppName());
        log.info("bodyStr: {}", bodyStr);
        CallbackBodyDTO bodyDTO = xmlMapper.readValue(bodyStr, CallbackBodyDTO.class);
        String encrypt = bodyDTO.getEncrypt();
        // 获取消息处理器
        WWXConfig.App appConfig = getAppConfig();
        MessageHelper messageHelper = MessageHelperFactory.createMessageHelper(appConfig, ReceiveIdGetter.INSTRUCT_CALLBACK.apply(appConfig));
        // 签名验证
        messageHelper.verifySignature(queryDTO, encrypt);
        // 加密数据解密
        String decrypt = messageHelper.decrypt(encrypt);
        JsonNode root = readXml2JsonNode(decrypt);

        JsonNode infoType = root.get("InfoType");
        if (infoType != null) {
            //JsonNode permanentCode = WWXBizHelper.getPermanentCode(root);
            //WWXThirdCompanyDO companyDO = WWXBizHelper.permanentCodeMap2Company(permanentCode);
        }
        return "success";
    }

    String[] LOG_FIELD_NAMES = {"Event", "InfoType"};

    default JsonNode readXml2JsonNode(String xml) throws Exception {
        Logger log = getLogger();
        JsonNode root = xmlMapper.readTree(xml);
        for (String logFieldName : LOG_FIELD_NAMES) {
            if (root.has(logFieldName)) {
                log.info("{} -> {}", logFieldName, root.get(logFieldName));
                break;
            }
        }
        log.info("消息解密结果: \n{}", root.toPrettyString());
        return root;
    }
}
