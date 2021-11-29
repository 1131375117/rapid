package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesCommonListVO {



	/**
	 * 标题
	 */
	@ApiModelProperty("标题")
	private String title;

	/**
	 * 来源
	 */
	@ApiModelProperty("来源")
	private String docSource;

	/**
	 * 发布日期
	 */
	@ApiModelProperty("发布日期")
	private LocalDate releaseDate;
}
