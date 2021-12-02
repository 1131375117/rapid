package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.RangeField;
import cn.huacloud.taxpreference.common.entity.dtos.LocalDateRangeQueryDTO;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangkh
 */
@Getter
@Setter
public class PoliciesExplainSearchQueryDTO extends AbstractHighlightPageQueryDTO {

    @ApiModelProperty("所属税种")
    @FilterField("taxCategories.codeValue")
    private String taxCategoriesCode;

    @ApiModelProperty("发布日期")
    @RangeField("releaseDate")
    private LocalDateRangeQueryDTO releaseDate;

    @ApiModelProperty("所属区域码值")
    @FilterField(value = "area.codeValue", withChildren = true)
    private String areaCode;

    @ApiModelProperty("适用行业码值")
    @FilterField(value = "industries.codeValue", withChildren = true)
    private List<String> industryCodes;

    @ApiModelProperty("纳税人资格认定类型码值")
    @FilterField("taxpayerIdentifyTypes.codeValue")
    private List<String> taxpayerIdentifyTypeCodes;

    @ApiModelProperty("适用企业类型码值")
    @FilterField("enterpriseTypes.codeValue")
    private List<String> enterpriseTypesCodes;


    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getPoliciesExplain().getAlias();
    }

    @Override
    public List<String> searchFields() {
        return Arrays.asList("title", "combinePlainContent");
    }

}
