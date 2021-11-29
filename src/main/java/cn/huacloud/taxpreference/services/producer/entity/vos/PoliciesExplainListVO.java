package cn.huacloud.taxpreference.services.producer.entity.vos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesExplainListVO extends PoliciesCommonListVO {

	@ApiModelProperty("政策解读id")
	private Long id;

}
