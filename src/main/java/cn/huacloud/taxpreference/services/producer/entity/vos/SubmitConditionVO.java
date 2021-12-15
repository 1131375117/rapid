package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

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
    @NotEmpty(message = "条件名称不能为空",groups = ValidationGroup.Manual.class)
    private String conditionName;

    /**
     * 具体要求
     */
    @ApiModelProperty("具体要求")
    @NotEmpty(message = "具体要求不能为空",groups = ValidationGroup.Manual.class)
    private String requirement;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private Long sort;

}
