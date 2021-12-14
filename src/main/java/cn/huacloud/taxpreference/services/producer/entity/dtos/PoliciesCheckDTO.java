package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesCheckDTO {
	@ApiModelProperty("政策法规id")
	private Long id;

	@ApiModelProperty("标题")
	@NotEmpty(message = "标题不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
	private String title;

}
