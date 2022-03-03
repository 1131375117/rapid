package cn.huacloud.taxpreference.services.user.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 用户名称修改dto
 *
 * @author fuhua
 **/
@Data
public class ConsumerNameUpdateDTO {

    @ApiModelProperty(value = "用户账户", example = "18390333725")
    @NotEmpty(message = "账号不能为空")
    private String account;

    @ApiModelProperty(value = "用户名称", example = "fuhua修改用户昵称")
    @NotEmpty(message = "用户名称不能为空")
    private String username;
}
