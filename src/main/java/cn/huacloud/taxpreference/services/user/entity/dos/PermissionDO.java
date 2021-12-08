package cn.huacloud.taxpreference.services.user.entity.dos;

import cn.huacloud.taxpreference.common.enums.PermissionGroup;
import cn.huacloud.taxpreference.common.enums.UserType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 权限数据实体
 * @author wangkh
 */
@Data
@TableName("t_permission")
public class PermissionDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户类型
     */
    private UserType userType;

    /**
     * 权限名称
     */
    private String permissionName;
    /**
     * 权限码值
     */
    private String permissionCode;
    /**
     * 权限分组
     */
    private PermissionGroup permissionGroup;
}
