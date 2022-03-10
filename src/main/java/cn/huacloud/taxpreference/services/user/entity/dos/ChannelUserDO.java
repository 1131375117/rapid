package cn.huacloud.taxpreference.services.user.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Data
@TableName("t_wwx_company")
public class ChannelUserDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long consumerUserId;
    private String channelName;
    private String channelType;
    private String openUserId;
    private String extendsField1;
    private String extendsField2;
    private String note;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean deleted;
}
