package cn.huacloud.taxpreference.services.wwx.impl;

import cn.huacloud.taxpreference.config.WWXConfig;
import cn.huacloud.taxpreference.services.wwx.WWXService;
import cn.huacloud.taxpreference.services.wwx.ase.WXBizMsgCrypt;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackBodyDTO;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackQueryDTO;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WWXServiceImpl implements WWXService {

    private final WXBizMsgCrypt wxBizMsgCrypt;

    private final WWXConfig wwxConfig;

    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public String verifyURL(CallbackQueryDTO queryDTO) throws Exception {
        return wxBizMsgCrypt.verifyURL(queryDTO.getMsgSignature(), queryDTO.getTimestamp(), queryDTO.getNonce(), queryDTO.getEchoStr());
    }

    @Override
    public String installCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception {
        log.info("开始注册回调");
        CallbackBodyDTO bodyDTO = xmlMapper.readValue(bodyStr, CallbackBodyDTO.class);
        String encrypt = bodyDTO.getEncrypt();
        // 签名验证
        wxBizMsgCrypt.verifySignature(queryDTO, encrypt);
        // 加密数据解密
        String decrypt = wxBizMsgCrypt.decrypt(encrypt);
        log.info("回调解密数据：{}", decrypt);
        return "success";
    }

    @Override
    public String dataCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception {
        log.info("开始数据回调");
        CallbackBodyDTO bodyDTO = xmlMapper.readValue(bodyStr, CallbackBodyDTO.class);
        String encrypt = bodyDTO.getEncrypt();
        // 签名验证
        wxBizMsgCrypt.verifySignature(queryDTO, encrypt);
        // 加密数据解密
        String decrypt = wxBizMsgCrypt.decrypt(encrypt);
        log.info("回调解密数据：{}", decrypt);
        return "success";
    }

    @Override
    public String instructCallback(CallbackQueryDTO queryDTO, String bodyStr) throws Exception {
        log.info("开始指令回调");
        CallbackBodyDTO bodyDTO = xmlMapper.readValue(bodyStr, CallbackBodyDTO.class);
        String encrypt = bodyDTO.getEncrypt();
        // 签名验证
        wxBizMsgCrypt.verifySignature(queryDTO, encrypt);
        // 加密数据解密
        String decrypt = wxBizMsgCrypt.decrypt(encrypt);
        log.info("回调解密数据：{}", decrypt);
        return "success";
    }
}
