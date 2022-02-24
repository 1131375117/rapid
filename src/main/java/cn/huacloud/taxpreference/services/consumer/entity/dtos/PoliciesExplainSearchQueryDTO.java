package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.RangeField;
import cn.huacloud.taxpreference.common.annotations.WithChildrenCodes;
import cn.huacloud.taxpreference.common.entity.dtos.LocalDateRangeQueryDTO;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangkh
 */
@Getter
@Setter
public class PoliciesExplainSearchQueryDTO extends AbstractHighlightPageQueryDTO {

    @ApiModelProperty(value = "所属税种",example = "")
    @FilterField("taxCategories.codeValue")
    private String taxCategoriesCode;

    @ApiModelProperty("发布日期")
    @RangeField("releaseDate")
    private LocalDateRangeQueryDTO releaseDate;

    @ApiModelProperty(value = "所属区域码值",example = "中央")
    @WithChildrenCodes(SysCodeType.AREA)
    @FilterField(value = "area.codeValue")
    private String areaCode;

    @ApiModelProperty(value = "适用行业码值",dataType = "String[]",example = "")
    @WithChildrenCodes(SysCodeType.INDUSTRY)
    @FilterField(value = "industries.codeValue")
    private List<String> industryCodes;

    @ApiModelProperty(value = "纳税人资格认定类型码值",dataType = "String[]",example = "")
    @FilterField("taxpayerIdentifyTypes.codeValue")
    private List<String> taxpayerIdentifyTypeCodes;

    @ApiModelProperty(value = "适用企业类型码值",dataType = "String[]",example = "")
    @FilterField("enterpriseTypes.codeValue")
    private List<String> enterpriseTypesCodes;


    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getPoliciesExplain().getAlias();
    }
}
