package cn.huacloud.taxpreference.services.user.entity.dos;

import cn.huacloud.taxpreference.common.enums.user.CreateWay;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户实体
 * @author wangkh
 */
@Data
@Accessors(chain = true)
@TableName("t_consumer_user")
public class ConsumerUserDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户账户
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
     * 电话号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 角色码值集合（以","分隔）
     */
    private String roleCodes;

    /**
     * 创建方式
     */
    private CreateWay createWay;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已删除
     */
    private Boolean deleted;
}
