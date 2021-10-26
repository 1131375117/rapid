package cn.huacloud.taxpreference.services.user.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangkh
 */
@ApiModel("角色列表视图")
@Data
public class RoleListVO {

    @ApiModelProperty("角色ID")
    private Long id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色码值")
    private String roleCode;

    @ApiModelProperty("权限码值集合（以\",\"分隔）")
    private String permissionCodes;

    @ApiModelProperty("角色描述")
    private String note;

    @ApiModelProperty("角色成员")
    private String userCount;
}
