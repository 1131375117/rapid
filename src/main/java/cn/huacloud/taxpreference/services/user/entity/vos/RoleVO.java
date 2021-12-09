package cn.huacloud.taxpreference.services.user.entity.vos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.enums.user.UserType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 角色视图
 * @author wangkh
 */
@Data
@ApiModel("角色视图")
public class RoleVO {
    @NotNull(message = "角色ID不能为空", groups = ValidationGroup.Update.class)
    @ApiModelProperty("主键ID")
    private Long id;
    @ApiModelProperty("用户类型")
    private UserType userType;
    @NotEmpty(message = "角色名称不能为空", groups = {ValidationGroup.Create.class, ValidationGroup.Update.class})
    @ApiModelProperty("角色名称")
    private String roleName;
    @NotEmpty(message = "角色码值不能为空", groups = {ValidationGroup.Create.class})
    @ApiModelProperty("角色码值")
    private String roleCode;
    @ApiModelProperty("角色描述")
    private String note;
}
