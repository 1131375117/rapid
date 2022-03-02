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

    public static MessageHelper createMessageHelper(WWXConfig.App app, String receiveId) {
        WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(app.getToken(), app.getEncodingAesKey(), receiveId);
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
