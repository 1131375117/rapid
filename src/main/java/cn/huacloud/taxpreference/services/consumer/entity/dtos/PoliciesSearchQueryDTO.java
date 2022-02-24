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

    @ApiModelProperty(value = "文号字号",example = "国家税务总局")
    private String docWordCode;

    @ApiModelProperty(value = "文号年号",example = "2021")
    @FilterField("docYearCode")
    private Integer docYearCode;

    @ApiModelProperty(value = "文号编号",example = "30")
    @FilterField("docNumCode")
    private Integer docNumCode;

    @ApiModelProperty(value = "所属税种",example = "10104")
    @FilterField("taxCategories.codeValue")
    private String taxCategoriesCode;

    @ApiModelProperty(value = "发布日期")
    @RangeField("releaseDate")
    private LocalDateRangeQueryDTO releaseDate;

    @ApiModelProperty(value = "所属区域码值",example = "中央")
    @WithChildrenCodes(SysCodeType.AREA)
    @FilterField(value = "area.codeValue")
    private String areaCode;

    @ApiModelProperty(value = "适用行业码值")
    @WithChildrenCodes(SysCodeType.INDUSTRY)
    @FilterField(value = "industries.codeValue")
    private List<String> industryCodes;

    @ApiModelProperty(value = "纳税人资格认定类型码值")
    @FilterField("taxpayerIdentifyTypes.codeValue")
    private List<String> taxpayerIdentifyTypeCodes;

    @ApiModelProperty(value = "适用企业类型码值")
    @FilterField("enterpriseTypes.codeValue")
    private List<String> enterpriseTypesCodes;

    @ApiModelProperty(value = "有效性", notes = "INVALID:失效;FULL_TEXT_VALID:全文有效;FULL_TEXT_REPEAL:全文废止;PARTIAL_VALID:部分有效;CLAUSE_INVALIDITY:条款失效;FULL_TEXT_INVALIDATION:全文失效;PARTIAL_REPEAL:部分废止;CLAUSE_REPEAL:条款废止",example = "FULL_TEXT_VALID")
    @FilterField("validity.codeValue")
    public ValidityEnum validity;

    @ApiModelProperty(value = "所属专题")
    @FilterField("specialSubject")
    private List<String> specialSubjects;

    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getPolicies().getAlias();
    }

}
