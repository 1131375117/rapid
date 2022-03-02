package cn.huacloud.taxpreference.services.wwx.impl;

import cn.huacloud.taxpreference.config.WWXConfig;
import cn.huacloud.taxpreference.services.wwx.TaxToolWWXService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * @author wangkh
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TaxToolWWXServiceImpl implements TaxToolWWXService {

    private final WWXConfig wwxConfig;

    @Override
    public String getAppName() {
        return "税务小工具";
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public WWXConfig.App getAppConfig() {
        return wwxConfig.getTaxTool();
    }
}
