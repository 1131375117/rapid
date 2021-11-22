package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * 政策解读数据传输对象
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesExplainDTO {


	@ApiModelProperty("id")
	private Long id;

	@NotEmpty(message = "标题不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
	@ApiModelProperty("标题")
	private String title;

	@NotEmpty(message = "来源不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
	@ApiModelProperty("来源")
	private String docSource;

	@NotNull(message = "发布日期不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
	@ApiModelProperty("发布日期")
	private LocalDate releaseDate;

	@NotEmpty(message = "正文不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
	@ApiModelProperty("正文")
	private String content;

	@ApiModelProperty("政策法规id")
	private Long policiesId;

}