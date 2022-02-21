package cn.huacloud.taxpreference.services.user.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author wangkh
 */
@Data
public class ConsumerLoginUserVO {
    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("用户账户")
    private String userAccount;
    @ApiModelProperty("用户名称、昵称")
    private String username;
    @ApiModelProperty(hidden = true)
    private String password;
    @ApiModelProperty("用户角色")
    private List<String> roleCodes;
    @ApiModelProperty("用户权限")
    private List<String> permissionCodes;
    @ApiModelProperty("手机号")
    private String phoneNumber;
}
