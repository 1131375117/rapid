package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.RangeField;
import cn.huacloud.taxpreference.common.entity.dtos.LocalDateRangeQueryDTO;
import cn.huacloud.taxpreference.services.producer.entity.enums.ValidityEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
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
    @FilterField("area.codeValue")
    private String areaCode;

    @ApiModelProperty("适用行业码值")
    @FilterField("industries.codeValue")
    private List<String> industryCodes;

    @ApiModelProperty("纳税人资格认定类型码值")
    @FilterField("taxpayerIdentifyTypes.codeValue")
    private List<String> taxpayerIdentifyTypeCodes;

    @ApiModelProperty("适用企业类型码值")
    @FilterField("enterpriseTypes.codeValue")
    private List<String> enterpriseTypesCodes;

    @Override
    public List<String> indexList() {
        return Collections.singletonList("policies_explain");
    }

    @Override
    public List<String> searchFields() {
        return Arrays.asList("title", "combinePlainContent");
    }

}
