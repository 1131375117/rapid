package cn.huacloud.taxpreference.services.user.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 密码登录
 * @author wangkh
 */
@Data
public class PasswordLoginDTO {
    @ApiModelProperty("用户账户、手机号")
    private String account;
    @ApiModelProperty("加密后的密码")
    private String password;
    @ApiModelProperty("图形验证码ID")
    private String captchaId;
    @ApiModelProperty("图形验证码")
    private String captchaCode;
}
