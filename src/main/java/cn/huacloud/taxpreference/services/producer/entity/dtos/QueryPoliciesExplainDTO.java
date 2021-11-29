package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 政策解读VO
 *
 * @author wuxin
 */
@Data
@ApiModel
public class QueryPoliciesExplainDTO extends KeywordPageQueryDTO {

	@ApiModelProperty("政策解读id")
	private Long id;
	@ApiModelProperty("政策解读关联政策id")
	private Long policiesId;

	@ApiModelProperty("热门问答关联政策id")
	private List<String> policiesIds;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("来源")
	private String docSource;

	@ApiModelProperty("发布日期")
	private LocalDate releaseDate;

	@ApiModelProperty("开始日期")
	private LocalDate startTime;

	@ApiModelProperty("结束日期")
	private LocalDate endTime;

	@ApiModelProperty("更新日期")
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "排序", notes = "RELEASE_DATE:发布时间,UPDATE_TIME:更新时间")
	private PoliciesSortType sortField;

	@Override
	public void paramReasonable() {
		super.paramReasonable();
		stringParamNullOrTrim();
	}
}
