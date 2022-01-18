package cn.huacloud.taxpreference.services.producer.entity.dtos;


import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import cn.huacloud.taxpreference.services.producer.entity.vos.TaxPreferenceCountVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 政策法规数据传输对象组合
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesCombinationDTO {

	@ApiModelProperty("政策法规主键id")
	private Long id;

	@ApiModelProperty("标题")
	@NotEmpty(message = "标题不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
	private String title;

//	@ApiModelProperty("文号")
//	private String docCode;

	@ApiModelProperty("字号")
	private String docWordCode;

	@ApiModelProperty("年号")
	private Integer docYearCode;

	@ApiModelProperty("编号")
	private Integer docNumCode;

	@ApiModelProperty("所属区域码值")
	@NotEmpty(message = "所属区域码值不能为空", groups = {ValidationGroup.Manual.class})
	private String areaCode;

	@ApiModelProperty("来源")
	@NotEmpty(message = "来源不能为空", groups = {ValidationGroup.Manual.class})
	private String docSource;

	@ApiModelProperty("所属税种码值")
	@NotEmpty(message = "所属税种码值不能为空", groups = {ValidationGroup.Manual.class})
	private List<String> taxCategoriesCodes;

	@ApiModelProperty("纳税人资格认定类型码值")
//	@NotEmpty(message = "纳税人资格认定类型码值不能为空", groups = {ValidationGroup.Manual.class})
	private List<String> taxpayerIdentifyTypeCodes;

	@ApiModelProperty("适用企业类型码值")
//	@NotEmpty(message = "适用企业类型码值不能为空", groups = {ValidationGroup.Manual.class})
	private List<String> enterpriseTypeCodes;

	@ApiModelProperty("适用行业码值")
	@NotEmpty(message = "适用行业码值不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
	private List<String> industryCodes;

	@ApiModelProperty("有效性")
	@NotNull(message = "有效性不能为空", groups = {ValidationGroup.Manual.class})
	private ValidityEnum validity;

	@ApiModelProperty("发布日期")
	@NotNull(message = "发布日期不能为空", groups = {ValidationGroup.Manual.class})
	private LocalDate releaseDate;

	@ApiModelProperty("所属专题")
	private List<String> specialSubjects;

	@ApiModelProperty("摘要")
//	@NotEmpty(message = "摘要不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
	private String digest;

	@ApiModelProperty("正文")
	@NotEmpty(message = "正文不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class , ValidationGroup.Manual.class})
	private String content;

	@ApiModelProperty("标签管理")
	private List<String> labels;

	@ApiModelProperty("废止说明")
	private String abolishNote;


/*	@ApiModelProperty("热点问答集合")
	@Valid
	private List<FrequentlyAskedQuestionDTO> frequentlyAskedQuestionDTOList;*/

	@ApiModelProperty("政策解读对象")
	@Valid
	private PoliciesExplainDTO policiesExplainDTO;

	@ApiModelProperty(hidden = true,name = "用户id")
	private Long inputUserId;

	@ApiModelProperty("关联的税收优惠列表")
	List<TaxPreferenceCountVO> taxPreferenceCountVOS;

	@ApiModelProperty("数据来源网站链接")
	private String spiderUrl;

	@ApiModelProperty("是否关联解读")
	private Boolean flag;

}