package cn.huacloud.taxpreference.services.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信找回密码验证码处理器
 * @author wangkh
 */
@RequiredArgsConstructor
@Component
public class SmsBizConsumerSubscribeCodeHandler implements SmsBizHandler {


    @Override
    public List<String> getParams(List<String> phoneNumbers) {
        return new ArrayList<>();
    }
}
