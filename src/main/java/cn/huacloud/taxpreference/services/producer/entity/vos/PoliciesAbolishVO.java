package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wuxin
 */
@Data
public class PoliciesAbolishVO {


    @ApiModelProperty("废止状态")
    private String policiesStatus;

    @ApiModelProperty("废止说明")
    private String abolishNote;

    @ApiModelProperty("税收优惠名称集合")
    private List<TaxPreferenceVO> taxPreferenceVOS;
}
