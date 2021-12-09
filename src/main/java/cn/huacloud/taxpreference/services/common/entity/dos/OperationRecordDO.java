package cn.huacloud.taxpreference.services.common.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 操作记录实体
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
@TableName("t_operation_record")
public class OperationRecordDO {
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
     * 操作类型
     */
    private String operationType;

    /**
     * 操作参数
     */
    private String operationParam;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
