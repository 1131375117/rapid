package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
    private Boolean useRecommend;

    @ApiModelProperty("所属税种")
    @FilterField("taxCategories.codeValue")
    private List<String> taxCategoriesCodes;

    @ApiModelProperty("企业类型")
    @FilterField("enterpriseType")
    private List<String> enterpriseTypes;

    @ApiModelProperty("减免事项")
    @FilterField("taxPreferenceItem")
    private List<String> taxPreferenceItems;

    @ApiModelProperty("纳税人登记注册类型码值")
    @FilterField("taxpayerRegisterType.codeValue")
    private String taxpayerRegisterTypeCode;

    @ApiModelProperty("动态筛选条件")
    private List<ConditionQuery> conditions;

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
                return Arrays.asList("taxPreferenceName", "combinePlainContent", "combinePoliciesTitle", "combinePoliciesDigest");
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

    @Data
    public static class ConditionQuery {
        /**
         * 条件名称
         */
        @ApiModelProperty("条件名称")
        private String conditionName;

        /**
         * 具体要求
         */
        @ApiModelProperty("具体要求集合")
        private List<String> conditionValues;
    }
}
