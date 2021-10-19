package cn.huacloud.taxpreference.services.user.entity.vos;

import cn.huacloud.taxpreference.common.enums.UserType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author wangkh
 */
@Data
public class LoginUserVO {
    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("用户账户")
    private String userAccount;
    @ApiModelProperty("用户名称、昵称")
    private String username;
    @ApiModelProperty("用户类型")
    private UserType userType;
    @ApiModelProperty("用户角色")
    private List<String> roleCodes;
    @ApiModelProperty("用户权限")
    private List<String> permissionCodes;
}
