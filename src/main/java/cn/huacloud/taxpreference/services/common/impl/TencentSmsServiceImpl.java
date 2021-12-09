package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.enums.SmsBiz;
import cn.huacloud.taxpreference.services.common.SmsService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 腾讯云短信服务实现
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class TencentSmsServiceImpl implements SmsService {

    private final SysParamService sysParamService;

    @Override
    public void sendSms(List<String> phoneNumbers, SmsBiz smsBiz, List<String> params) {

    }
}
