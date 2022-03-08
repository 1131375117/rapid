package cn.huacloud.taxpreference.services.consumer.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * t_subscribe
 * @author 
 */
@ApiModel(value="cn/huacloud/taxpreference/services/consumer/entity/dos.SubscribeDO订阅表")
@Data
@TableName("t_subscribe")
public class SubscribeDO implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty(value="主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消费者用户ID
     */
    @ApiModelProperty(value="消费者用户ID")
    private Long consumerUserId;

    /**
     * 订阅类型
     */
    @ApiModelProperty(value="订阅类型")
    private String subscribeType;

    /**
     * 数据源ID
     */
    @ApiModelProperty(value="数据源ID")
    private Long sourceId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}