package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.enums.KeywordType;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesSortType;
import cn.huacloud.taxpreference.services.producer.entity.enums.PoliciesStatusEnum;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 政策法规条件类
 *
 * @author wuxin
 */
@Data
@ApiModel
public class QueryPoliciesDTO extends KeywordPageQueryDTO {

	@ApiModelProperty("政策法规id")
	private Long id;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("文号")
	private String docCode;

	@ApiModelProperty("字号")
	private String docWordCode;

	@ApiModelProperty("年号")
	private Integer docYearCode;

	@ApiModelProperty("编号")
	private Integer docNumCode;

	@ApiModelProperty("所属税种码值")
	private List<String> taxCategoriesCodes;

	@ApiModelProperty("纳税人资格认定类型码值")
	private List<String> taxpayerIdentifyTypeCodes;

	@ApiModelProperty("适用企业类型码值")
	private List<String> enterpriseTypeCodes;

	@ApiModelProperty("适用行业码值")
	private List<String> industryCodes;

	@ApiModelProperty("所属区域码值")
	private List<String> areaCode;

	@ApiModelProperty("有效性")
	private String validity;

	@ApiModelProperty("发布日期")
	private LocalDate releaseDate;

	@ApiModelProperty("开始时间")
	private LocalDate startTime;

	@ApiModelProperty("结束时间")
	private LocalDate endTime;

	@ApiModelProperty("更新日期")
	private LocalDateTime updateTime;

	@ApiModelProperty("排序字段,RELEASE_DATE:按照发布时间排序,UPDATE_TIME:按照更新时间排序")
	private PoliciesSortType policiesSortType;

	@ApiModelProperty("查询条件类型,TITLE:按照标题查询,DOC_CODE:按照文号查询")
	private KeywordType keywordType;

	@ApiModelProperty(value = "数据类型",notes = "REPTILE_SYNCHRONIZATION:爬虫数据，PUBLISHED:录入数据")
	private String policiesStatus;

	@Override
	public void paramReasonable() {
		super.paramReasonable();
		reasonableStringList(industryCodes, this::setIndustryCodes);
		reasonableStringList(enterpriseTypeCodes, this::setEnterpriseTypeCodes);
		reasonableStringList(taxpayerIdentifyTypeCodes, this::setTaxpayerIdentifyTypeCodes);
		reasonableStringList(areaCode, this::setAreaCode);

	}

	public void reasonableStringList(List<String> target, Consumer<List<String>> consumer) {
		if (target == null) {
			return;
		}
		List<String> collect =
				target.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
		if (collect.isEmpty()) {
			consumer.accept(null);
		} else {
			consumer.accept(collect);
		}
	}
}
