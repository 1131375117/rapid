package cn.huacloud.taxpreference.services.wework.crypt;

import cn.huacloud.taxpreference.services.wework.aes.WXBizMsgCrypt;
import cn.huacloud.taxpreference.services.wework.entity.model.AppConfig;
import cn.huacloud.taxpreference.services.wework.entity.model.CallbackQuery;
import lombok.SneakyThrows;

/**
 * @author wangkh
 */
public class MessageCryptFactory {

    @SneakyThrows
    public static MessageCrypt createMessageCrypt(AppConfig appConfig, String providerId) {
        WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(appConfig.getToken(), appConfig.getEncodingAesKey(), providerId);
        return new DefaultMessageCrypt(wxBizMsgCrypt);
    }

    private static class DefaultMessageCrypt implements MessageCrypt {

        private final WXBizMsgCrypt wxBizMsgCrypt;

        public DefaultMessageCrypt(WXBizMsgCrypt wxBizMsgCrypt) {
            this.wxBizMsgCrypt = wxBizMsgCrypt;
        }

        @Override
        public String verifyUrl(CallbackQuery query) throws Exception {
            return wxBizMsgCrypt.VerifyURL(query.getMsg_signature(), query.getTimestamp(), query.getNonce(), query.getEchostr());
        }

        @Override
        public String decryptMsg(CallbackQuery query, String postData) throws Exception {
            return wxBizMsgCrypt.DecryptMsg(query.getMsg_signature(), query.getTimestamp(), query.getNonce(), postData);
        }
    }
}
