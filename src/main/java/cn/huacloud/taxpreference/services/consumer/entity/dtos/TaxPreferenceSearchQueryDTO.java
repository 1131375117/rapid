package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.common.annotations.WithChildrenCodes;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 税收优惠分页检索对象
 * @author wangkh
 */
@Getter
@Setter
public class TaxPreferenceSearchQueryDTO extends AbstractHighlightPageQueryDTO {

    @ApiModelProperty("是否使用推荐")
    @Deprecated
    private Boolean useRecommend;

    @ApiModelProperty("所属税种")
    @FilterField("taxCategories.codeValue")
    private List<String> taxCategoriesCodes;

    @ApiModelProperty("纳税人登记注册类型码值")
    @FilterField("taxpayerRegisterType.codeValue")
    private String taxpayerRegisterTypeCode;

    @ApiModelProperty("纳税人登记注册类型码值")
    @FilterField("taxpayerType.codeValue")
    private String taxpayerTypeCode;

    @ApiModelProperty("适用行业码值")
    @WithChildrenCodes(SysCodeType.INDUSTRY)
    @FilterField(value = "industries.codeValue")
    private List<String> industryCodes;

    @ApiModelProperty("适用企业类型码值")
    @FilterField("enterpriseTypes.codeValue")
    private List<String> enterpriseTypesCodes;

    @ApiModelProperty("纳税信用等级")
    @FilterField("taxpayerCreditRatings")
    private List<String> taxpayerCreditRatings;

    @ApiModelProperty("标签")
    private String label;

    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getTaxPreference().getAlias();
    }

    @Override
    public List<String> searchFields() {
        switch (getSearchScope()) {
            case CONTENT:
                return Collections.singletonList("combinePlainContent");
            case TITLE_AND_CONTENT:
                return Arrays.asList("taxPreferenceName", "combinePlainContent");
            default:
                return Collections.singletonList("taxPreferenceName");
        }
    }

    @Override
    public Set<String> notFragmentHighlightFields() {
        return Collections.singleton("taxPreferenceName");
    }

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (useRecommend == null) {
            // 默认不使用推荐
            useRecommend = false;
        }
    }
}
