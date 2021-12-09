package cn.huacloud.taxpreference.services.user.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 找回密码数据对象
 * @author wangkh
 */
@Data
public class RetrievePasswordDTO {
    @ApiModelProperty("手机号")
    private String phoneNumber;
    @ApiModelProperty("短信验证码")
    private String verificationCode;
    @ApiModelProperty("加密后的密码")
    private String password;
}
