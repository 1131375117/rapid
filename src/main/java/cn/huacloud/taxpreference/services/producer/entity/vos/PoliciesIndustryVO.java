package cn.huacloud.taxpreference.services.producer.entity.vos;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesIndustryVO {
	@ApiModelProperty("政策法规id")
	private Long id;
	@ApiModelProperty("所属行业名称")
	private String industryNames;
}
