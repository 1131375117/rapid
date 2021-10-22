package cn.huacloud.taxpreference.services.user.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户列表视图
 * @author wangkh
 */
@Data
@ApiModel
public class UserListVO {
    @ApiModelProperty("用户ID")
    private Long id;
    @ApiModelProperty("用户账户")
    private String userAccount;
    @ApiModelProperty("用户名称")
    private String username;
    @ApiModelProperty("用户角色")
    private List<RoleVO> roles;
    @ApiModelProperty("是否禁用")
    private Boolean disable;
}
