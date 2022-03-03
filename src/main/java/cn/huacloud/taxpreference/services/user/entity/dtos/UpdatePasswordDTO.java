package cn.huacloud.taxpreference.services.user.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 修改密码对象
 *
 * @author 付华
 */
@Data
public class UpdatePasswordDTO {
    @ApiModelProperty(value = "手机号", example = "18390333725")
    private String phoneNumber;
    @ApiModelProperty("短信验证码")
    private String verificationCode;
    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty("加密后密码")
    private String oldPassword;
    @NotEmpty(message = "请再次输入密码")
    @ApiModelProperty("第二次密码")
    private String newPassword;
}
