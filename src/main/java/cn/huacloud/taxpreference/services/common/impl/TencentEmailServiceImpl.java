package cn.huacloud.taxpreference.services.common.impl;

import cn.huacloud.taxpreference.common.enums.EmailBiz;
import cn.huacloud.taxpreference.services.common.EmailService;
import cn.huacloud.taxpreference.services.common.SysParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 邮件服务实现
 * @author wangkh
 */
@RequiredArgsConstructor
@Service
public class TencentEmailServiceImpl implements EmailService {

    private final SysParamService sysParamService;

    @Override
    public void sendEmail(List<String> destinations, EmailBiz emailBiz, Map<String, String> params) {

    }
}
