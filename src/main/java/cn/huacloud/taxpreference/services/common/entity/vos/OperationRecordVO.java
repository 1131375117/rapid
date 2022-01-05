package cn.huacloud.taxpreference.services.common.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 操作记录实体
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
public class OperationRecordVO {
    /**
     * 主键id,自动递增
     */
    @ApiModelProperty("id唯一")
    private Long sourceId;


    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("浏览时间")
    private LocalDate viewTime;
}
