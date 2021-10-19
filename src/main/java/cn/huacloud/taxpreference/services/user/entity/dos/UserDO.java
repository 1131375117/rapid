package cn.huacloud.taxpreference.services.user.entity.dos;

import cn.huacloud.taxpreference.common.enums.UserType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 用户实体
 * @author wangkh
 */
@Data
@TableName("t_user")
public class UserDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名称
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户姓名、昵称
     */
    private String username;

    /**
     * 用户类型
     */
    private UserType userType;

    /**
     * 角色码值集合（以","分隔）
     */
    private String roleCodes;

    /**
     * 创建时间
     */
    private Date createTime;
}
