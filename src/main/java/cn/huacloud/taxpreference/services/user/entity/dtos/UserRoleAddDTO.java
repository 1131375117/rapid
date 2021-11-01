package cn.huacloud.taxpreference.services.user.entity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 为用户角色添加视图
 * @author wangkh
 */
@Data
public class UserRoleAddDTO {
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("要添加的角色码值")
    private String addRoleCode;
}
