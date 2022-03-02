package cn.huacloud.taxpreference.services.wwx.constants;

import cn.huacloud.taxpreference.config.WWXConfig;
import cn.huacloud.taxpreference.services.wwx.entity.dtos.CallbackBodyDTO;

import java.util.function.Function;

/**
 * @author wangkh
 */
public interface ReceiveIdGetter {
    Function<WWXConfig.App, String> VERIFY_URL = WWXConfig.App::getCorpId;
    Function<WWXConfig.App, String> INSTALL_CALLBACK = WWXConfig.App::getSuiteId;
    Function<CallbackBodyDTO, String> DATA_CALLBACK = CallbackBodyDTO::getToUserName;
    Function<WWXConfig.App, String> INSTRUCT_CALLBACK = WWXConfig.App::getSuiteId;
}
