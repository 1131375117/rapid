package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 流程查询接口VO对象
 * @author: fuhua
 * @create: 2021-10-25 14:59
 **/
@Data
public class ProcessListVO {
    @ApiModelProperty("流程审批id")
    private String id;
    @ApiModelProperty("优惠事项名称")
    private String taxPreferenceName;
    @ApiModelProperty("所属税种")
    private String taxCategoriesName;
    @ApiModelProperty("审批状态")
    private String processStatus;
    @ApiModelProperty("时间")
    private LocalDateTime createTime;
    @ApiModelProperty("录入人用户")
    private String userName;
}
