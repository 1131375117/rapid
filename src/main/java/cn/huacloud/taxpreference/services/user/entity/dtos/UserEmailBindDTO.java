package cn.huacloud.taxpreference.services.user.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 邮箱绑定
 *
 * @author fuhua
 **/
@Data
public class UserEmailBindDTO {
    @ApiModelProperty("邮箱")
    @NotEmpty(message = "验证码不能为空")
    private String email;
    @ApiModelProperty("邮箱验证码")
    @NotEmpty(message = "邮箱验证码不能为空")
    private String verificationCode;

}
