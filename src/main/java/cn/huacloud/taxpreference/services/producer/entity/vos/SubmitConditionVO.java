package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 申报条件表VO
 * @author: fuhua
 * @create: 2021-10-21 10:08
 **/
@Data
public class SubmitConditionVO {

    /**
     * 条件名称
     */
    @ApiModelProperty("条件名称")
    private String conditionName;

    /**
     * 具体要求
     */
    @ApiModelProperty("具体要求")
    private String requirement;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Long sort;

}
