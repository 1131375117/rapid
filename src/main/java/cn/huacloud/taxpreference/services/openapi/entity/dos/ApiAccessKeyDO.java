package cn.huacloud.taxpreference.services.openapi.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Data
@TableName("t_api_access_key")
public class ApiAccessKeyDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 消费者用户ID
     */
    private Long consumerUserId;
    /**
     * accessKeyId
     */
    private String accessKeyId;
    /**
     * accessKeySecret
     */
    private String accessKeySecret;
    /**
     * 是否可用
     */
    private Boolean enable;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后使用时间
     */
    private LocalDateTime lastUseTime;
}
