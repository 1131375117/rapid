package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @description: 流程管理查询DTO
 * @author: fuhua
 * @create: 2021-10-25 14:35
 **/
@Data
public class ProcessListDTO extends PageQueryDTO {
    @ApiModelProperty("优惠事项名称")
    private String taxPreferenceName;
    @ApiModelProperty("税种码值")
    private String taxCategoriesCode;
    @ApiModelProperty("时间范围")
    private LocalDate startTime;
    @ApiModelProperty("时间范围")
    private LocalDate endTime;
    @ApiModelProperty("用户名")
    private String userName;
}