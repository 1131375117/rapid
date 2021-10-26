package cn.huacloud.taxpreference.services.producer.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 申报条件表实体
 * @author: fuhua
 * @create: 2021-10-21 10:08
 **/
@Data
public class SubmitConditionDTO {

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
/*    @ApiModelProperty("排序字段")
    private String sort;*/

}
