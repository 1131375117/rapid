package cn.huacloud.taxpreference.services.user.entity.vos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 生产者用户视图对象
 *
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class ProducerUserVO {

    @NotNull(message = "用户ID不能为空", groups = ValidationGroup.Update.class)
    @ApiModelProperty("主键ID")
    private Long id;

    @NotEmpty(message = "用户账号不能为空", groups = {ValidationGroup.Create.class, ValidationGroup.Update.class})
    @ApiModelProperty("用户账户")
    private String userAccount;

    @NotEmpty(message = "用户密码不能为空", groups = {ValidationGroup.Create.class, ValidationGroup.Update.class})
    @ApiModelProperty("用户密码")
    private String password;

    @NotEmpty(message = "用户姓名不能为空", groups = {ValidationGroup.Create.class, ValidationGroup.Update.class})
    @ApiModelProperty("用户姓名")
    private String username;

    @ApiModelProperty("电话号码")
    private String phoneNumber;

    @ApiModelProperty("身份证号码")
    private String idCardNo;

    @ApiModelProperty("邮箱")
    private String email;
}
