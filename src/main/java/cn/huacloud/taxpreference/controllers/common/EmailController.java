package cn.huacloud.taxpreference.controllers.common;

import cn.huacloud.taxpreference.common.enums.BizCode;
import cn.huacloud.taxpreference.common.enums.EmailBiz;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.message.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 邮件发送
 *
 * @author fuhua
 **/
@Api(tags = "邮件发送")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class EmailController {

    private final EmailService emailService;

    @ApiOperation("发送邮件")
    @PostMapping("/sendEmail")
    public ResultVO<Void> sendSms(String email, EmailBiz emailBiz) {
        if (StringUtils.isBlank(email) || email.length() < 11) {
            throw BizCode._4100.exception();
        }
        emailService.sendEmail(email, emailBiz);
        return ResultVO.ok();
    }

}
