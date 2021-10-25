package cn.huacloud.taxpreference.services.producer.entity.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 审批流程表
 * @author: fuhua
 * @create: 2021-10-25 09:30
 **/
@Data
@TableName("t_process")
public class ProcessDO {
    /**
     * 主键自增
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 税收优惠ID
     */
    private Long taxPreferenceId;

    /**
     * 创建人ID
     */
    private Long creatorId;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     *
     * 审批人ID
     */
    private String approverId;
    /**
     * 审批人名
     */
    private String approverName;
    /**
     * 审批时间
     */
    private LocalDateTime approvalTime;
    /**
     * 流程状态
     */
    private String processStatus;
    /**
     * 是否为最新流程
     */
    private Boolean latestProcess;
}
