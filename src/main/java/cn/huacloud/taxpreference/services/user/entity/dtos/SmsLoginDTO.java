package cn.huacloud.taxpreference.services.user.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 短信验证码登录
 * @author wangkh
 */
@Data
public class SmsLoginDTO {
    @ApiModelProperty("手机号")
    private String phoneNumber;
    @ApiModelProperty("短信验证码")
    private String verificationCode;
}
