package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.RangeField;
import cn.huacloud.taxpreference.common.annotations.WithChildrenCodes;
import cn.huacloud.taxpreference.common.entity.dtos.LocalDateRangeQueryDTO;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangkh
 */
@Getter
@Setter
public class PoliciesSearchQueryDTO extends AbstractHighlightPageQueryDTO {

    @ApiModelProperty("文号字号")
    private String docWordCode;

    @ApiModelProperty("文号年号")
    @FilterField("docYearCode")
    private Integer docYearCode;

    @ApiModelProperty("文号编号")
    @FilterField("docNumCode")
    private Integer docNumCode;

    @ApiModelProperty("所属税种")
    @FilterField("taxCategories.codeValue")
    private String taxCategoriesCode;

    @ApiModelProperty("发布日期")
    @RangeField("releaseDate")
    private LocalDateRangeQueryDTO releaseDate;

    @ApiModelProperty("所属区域码值")
    @WithChildrenCodes(SysCodeType.AREA)
    @FilterField(value = "area.codeValue")
    private String areaCode;

    @ApiModelProperty("适用行业码值")
    @WithChildrenCodes(SysCodeType.INDUSTRY)
    @FilterField(value = "industries.codeValue")
    private List<String> industryCodes;

    @ApiModelProperty("纳税人资格认定类型码值")
    @FilterField("taxpayerIdentifyTypes.codeValue")
    private List<String> taxpayerIdentifyTypeCodes;

    @ApiModelProperty("适用企业类型码值")
    @FilterField("enterpriseTypes.codeValue")
    private List<String> enterpriseTypesCodes;

    @ApiModelProperty(value = "有效性", notes = "INVALID:失效;FULL_TEXT_VALID:全文有效;FULL_TEXT_REPEAL:全文废止;PARTIAL_VALID:部分有效;CLAUSE_INVALIDITY:条款失效;FULL_TEXT_INVALIDATION:全文失效;PARTIAL_REPEAL:部分废止;CLAUSE_REPEAL:条款废止")
    @FilterField("validity.codeValue")
    public ValidityEnum validity;

    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getPolicies().getAlias();
    }

}
