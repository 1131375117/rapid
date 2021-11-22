package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class QueryAbolishDTO {
	@ApiModelProperty("政策法规id")
	private Long id;

	@ApiModelProperty("废止状态")
	@NotEmpty(message = "废止状态不能为空")
	private String validity;

	@ApiModelProperty("废止说明")
	@NotEmpty(message = "废止说明不能为空")
	private String abolishNote;

	@ApiModelProperty("税收优惠的id集合")
	private List<Long> ids;

}
