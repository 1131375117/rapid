package cn.huacloud.taxpreference.services.user.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色视图
 * @author wangkh
 */
@Data
@ApiModel("角色视图")
public class RoleVO {
    @ApiModelProperty("主键ID")
    private Long id;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("角色码值")
    private String roleCode;
}
