package cn.huacloud.taxpreference.services.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信登录验证码处理器
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class EmailBizConsultationReplyHandler implements EmailBizHandler {


    @Override
    public List<String> getParams(List<String> emails) {
        return new ArrayList<>();
    }
}
