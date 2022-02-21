package cn.huacloud.taxpreference.services.common.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * 操作记录实体
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
public class OperationRecordDTO {

    @ApiModelProperty(hidden = true)
    private Long id;

    /**
     * 操作类型
     */
    @ApiModelProperty(notes = "views.taxPreference->税收优惠浏览量," +
            "views.policies->政策法规浏览量," +
            "views.caseAnalysis->案例分析浏览量," +
            "views.policiesExplain->政策解读浏览量," +
            "views.frequentlyAskedQuestion->热门问答浏览量" +
            "views.consultation->热门咨询")
    @NotEmpty(message = "操作类型不能为空")
    private String operationType;

    /**
     * 操作参数
     */
    @ApiModelProperty("操作参数,浏览操作只需要给id,例如:5")
    private String operationParam;

}
