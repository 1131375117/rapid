package cn.huacloud.taxpreference.services.user.entity.dos;

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
@TableName("t_producer_user")
public class ProducerUserDO {

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
     * 身份证号码
     */
    private String idCardNo;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 角色码值集合（以","分隔）
     */
    private String roleCodes;

    /**
     * 是否已禁用
     */
    private Boolean disable;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已删除
     */
    private Boolean deleted;
}
