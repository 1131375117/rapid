package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fuhua
 **/
@Data
public class ConsultationCountVO {
    @ApiModelProperty("企业/人个数")
    private Long customTotals;
    @ApiModelProperty("问题个数")
    private Long questionTotals;
}
