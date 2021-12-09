package cn.huacloud.taxpreference.services.consumer.entity.dos;

import cn.huacloud.taxpreference.common.enums.CollectionType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 收藏实体
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
@TableName("t_collection")
public class CollectionDO {
    /**
     * 主键id,自动递增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消费者用户id
     */
    private Long consumerUserId;

    /**
     * 收藏类型
     */
    private CollectionType collectionType;

    /**
     * 数据源id
     */
    private Long sourceId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
