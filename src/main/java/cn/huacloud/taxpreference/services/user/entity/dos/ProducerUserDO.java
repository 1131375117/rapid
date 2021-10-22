package cn.huacloud.taxpreference.services.user.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 生产者用户实体
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
     * 用户ID
     */
    private Long userId;

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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否已删除
     */
    private Boolean deleted;
}
