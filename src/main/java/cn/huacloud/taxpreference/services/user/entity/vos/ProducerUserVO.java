package cn.huacloud.taxpreference.services.user.entity.vos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.common.enums.UserType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotEmpty;

/**
 * 生产者用户视图对象
 * @author wangkh
 */
public class ProducerUserVO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账户
     */
    @NotEmpty(message = "用户账号不能为空", groups = ValidationGroup.Create.class)
    private String userAccount;

    /**
     * 用户密码
     */
    @NotEmpty(message = "用户密码不能为空", groups = ValidationGroup.Create.class)
    private String password;

    /**
     * 用户姓名、昵称
     */
    @NotEmpty(message = "用户密码不能为空", groups = ValidationGroup.Create.class)
    private String username;

}
