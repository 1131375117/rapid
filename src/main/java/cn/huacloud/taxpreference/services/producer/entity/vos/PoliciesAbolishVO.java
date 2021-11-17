package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.services.producer.entity.dos.TaxPreferenceDO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wuxin
 */
@Data
public class PoliciesAbolishVO {


    @ApiModelProperty("废止状态")
    private ValidityEnum validityEnum;

    @ApiModelProperty("废止说明")
    private String abolishNote;

    @ApiModelProperty("税收优惠名称集合")
    private List<TaxPreferenceAbolishVO> taxPreferenceVOS;
}
