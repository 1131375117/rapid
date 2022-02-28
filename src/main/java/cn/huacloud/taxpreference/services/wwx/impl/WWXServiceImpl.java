package cn.huacloud.taxpreference.services.wwx.impl;

import cn.huacloud.taxpreference.config.WWXConfig;
import cn.huacloud.taxpreference.services.wwx.WWXService;
import cn.huacloud.taxpreference.services.wwx.ase.WXBizMsgCrypt;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallBackQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class WWXServiceImpl implements WWXService {

    private final WXBizMsgCrypt wxBizMsgCrypt;

    private final WWXConfig wwxConfig;

    @Override
    public String callBackGet(CallBackQueryDTO query) throws Exception {
        String msg = wxBizMsgCrypt.VerifyURL(query.getMsgSignature(), query.getTimestamp(), query.getNonce(), query.getEchoStr());
        return msg;
    }

    @Override
    public String callBackPost(CallBackQueryDTO query, String bodyStr) throws Exception {
        wxBizMsgCrypt.DecryptMsg(query.getMsgSignature(), query.getMsgSignature(), query.getNonce(), bodyStr);
        return null;
    }
}
