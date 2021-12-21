package cn.huacloud.taxpreference.services.producer.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author fuhua
 **/
@Data
public class ConditionDO {
    /**
     * 条件名称
     */
    @ApiModelProperty("条件名称")
    private String conditionName;

    /**
     * 具体要求
     */
    @ApiModelProperty("具体要求")
    private List<String> requirement;
}
