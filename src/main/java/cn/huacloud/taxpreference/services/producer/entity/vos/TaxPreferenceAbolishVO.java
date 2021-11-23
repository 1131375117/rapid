package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.enums.taxpreference.PreferenceValidation;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 税收优惠VO对象
 * @author: fuhua
 * @create: 2021-10-21 09:32
 **/
@Data
public class TaxPreferenceAbolishVO {
	@ApiModelProperty("税收优惠id")
	private Long id;

	@ApiModelProperty("税收优惠的名称")
	private String taxPreferenceName;

	@ApiModelProperty("有效性")
	private PreferenceValidation validity;


}
