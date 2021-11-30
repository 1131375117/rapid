package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 政策解读详情VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class FrequentlyAskedQuestionDetailVO extends PoliciesCommonListVO{

	/**
	 * 主键Id
	 */
	@ApiModelProperty("主键Id")
	private Long id;

	/**
	 * 正文
	 */
	@ApiModelProperty("正文")
	private String content;


	@ApiModelProperty("热门问答中政策id")
	private List<String> policiesIds;

	@ApiModelProperty("政策法规标题")
	private List<PoliciesTitleVO> policies;
}