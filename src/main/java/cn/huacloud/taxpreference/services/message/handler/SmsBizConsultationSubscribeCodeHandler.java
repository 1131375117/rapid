package cn.huacloud.taxpreference.services.message.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信订阅业务
 *
 * @author fuhua
 */
@RequiredArgsConstructor
@Component
public class SmsBizConsultationSubscribeCodeHandler implements SmsBizHandler {


    @Override
    public List<String> getParams(List<String> phoneNumbers) {
        return new ArrayList<>();
    }
}
