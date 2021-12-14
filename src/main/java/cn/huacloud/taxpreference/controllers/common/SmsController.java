package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.enums.SmsBiz;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.message.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangkh
 */
@Api(tags = "短信发送")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class SmsController {

    private final SmsService smsService;

    @ApiOperation("发送短信")
    @PostMapping("/sms")
    public ResultVO<Void> sendSms(String phoneNumber, SmsBiz smsBiz) {
        smsService.sendSms(phoneNumber, smsBiz);
        return ResultVO.ok();
    }
}