package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Getter
@Setter
public class ConditionSearchVO {
    /**
     * 条件名称
     */
    @ApiModelProperty("条件名称")
    private String conditionName;

    /**
     * 具体要求
     */
    @ApiModelProperty("具体要求")
    private List<String> conditionValues;
}
