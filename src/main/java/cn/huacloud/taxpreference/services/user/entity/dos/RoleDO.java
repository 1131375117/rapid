package cn.huacloud.taxpreference.services.user.entity.dos;

import cn.huacloud.taxpreference.common.enums.UserType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 权限数据实体
 * @author wangkh
 */
@Data
@Accessors(chain = true)
@TableName("t_role")
public class RoleDO {
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
     * 角色名称
     */
    private String roleName;
    /**
     * 角色码值
     */
    private String roleCode;
    /**
     * 权限码值集合（以","分隔）
     */
    private String permissionCodes;
    /**
     * 角色描述
     */
    private String note;
}
