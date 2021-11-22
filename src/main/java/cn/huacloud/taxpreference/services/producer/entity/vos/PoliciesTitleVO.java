package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesTitleVO {


	@ApiModelProperty("政策法规id")
	private Long id;
	@ApiModelProperty("政策解读中政策法规id")
	private Long policiesId;
	@ApiModelProperty("政策法规名称")
	private String title;
	@ApiModelProperty("是否关联true:关联,false:未关联")
	private Boolean relatedPolicy;
}
