package cn.huacloud.taxpreference.services.user.entity.vos;

import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 权限视图
 * @author wangkh
 */
@Data
public class PermissionVO {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("权限ID")
    private Long id;

    @ApiModelProperty("权限名称")
    private String permissionName;

    @ApiModelProperty("权限码值")
    private String permissionCode;

    @ApiModelProperty("权限分组")
    private PermissionGroup permissionGroup;
}
