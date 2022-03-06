package cn.huacloud.taxpreference.services.wwx.ase;

import cn.huacloud.taxpreference.config.WWXConfig;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackQueryDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * @author wangkh
 */
public class MessageHelperFactory {

    /**
     * 创建消息助手
     * @param appConfig 应用配置信息
     * @param receiveId 这个receiveId官方示例代码设计的非常沙雕，感觉完全没必要，你发起的回调你自己不验证让第三方验证。
     * @return 消息助手
     */
    public static MessageHelper createMessageHelper(WWXConfig.App appConfig, String receiveId) {
        WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(appConfig.getToken(), appConfig.getEncodingAesKey(), receiveId);
        return new DefaultMessageHelper(wxBizMsgCrypt);
    }

    @Slf4j
    private static final class DefaultMessageHelper implements MessageHelper {

        private final WXBizMsgCrypt wxBizMsgCrypt;

        public DefaultMessageHelper(WXBizMsgCrypt wxBizMsgCrypt) {
            this.wxBizMsgCrypt = wxBizMsgCrypt;
        }

        @Override
        public String verifyURL(CallbackQueryDTO queryDTO) {
            return wxBizMsgCrypt.VerifyURL(queryDTO.getMsgSignature(), queryDTO.getTimestamp(), queryDTO.getNonce(), queryDTO.getEchoStr());
        }

        @Override
        public String decrypt(String encrypt) {
            return wxBizMsgCrypt.decrypt(encrypt);
        }

        @Override
        public void verifySignature(CallbackQueryDTO queryDTO, String encrypt) {
            String signature = SHA1.getSHA1(wxBizMsgCrypt.token, queryDTO.getTimestamp(), queryDTO.getNonce(), encrypt);
            if (!signature.equals(queryDTO.getMsgSignature())) {
                throw new AesException(AesException.ValidateSignatureError);
            }
        }
    }
}
