package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 税收优惠VO对象
 * @author: fuhua
 * @create: 2021-10-21 09:32
 **/
@Data
public class TaxPreferenceAbolishVO{
    @ApiModelProperty("税收优惠id")
    private Long id;

    @ApiModelProperty("税收优惠的名称")
    private String taxCategoriesName;

}
