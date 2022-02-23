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
    private Long consumerUserId;
    private String accessKeyId;
    private String accessKeySecret;
    private Boolean enable;
    private LocalDateTime createTime;
    private LocalDateTime lastUseTime;
}
