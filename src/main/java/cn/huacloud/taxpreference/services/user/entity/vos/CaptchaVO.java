package cn.huacloud.taxpreference.services.user.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangkh
 */
@ApiModel("验证码视图")
@Data
public class CaptchaVO {
    @ApiModelProperty("验证码ID")
    private String captchaId;
    @ApiModelProperty("验证码图片Base64字符串")
    private String imageBase64;
}
