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
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("权限ID")
    private Long id;
    /**
     * 权限名称
     */
    @ApiModelProperty("权限名称")
    private String permissionName;
    /**
     * 权限码值
     */
    @ApiModelProperty("权限码值")
    private String permissionCode;
    /**
     * 权限分组
     */
    @ApiModelProperty("权限分组")
    private PermissionGroup permissionGroup;
}
