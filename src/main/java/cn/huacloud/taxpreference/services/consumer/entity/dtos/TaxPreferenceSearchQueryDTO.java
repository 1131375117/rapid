package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 税收优惠分页检索对象
 *
 * @author wangkh
 */
@Getter
@Setter
public class TaxPreferenceSearchQueryDTO extends AbstractHighlightPageQueryDTO {

    @ApiModelProperty(value = "是否使用推荐", example = "false")
    private Boolean useRecommend;

    @ApiModelProperty(value = "所属税种", example = "[\"10101\"]")
    @FilterField("taxCategories.codeValue")
    private List<String> taxCategoriesCodes;

    @ApiModelProperty(value = "企业类型", example = "[\"递归的方式\"]")
    @FilterField("enterpriseType")
    private List<String> enterpriseTypes;

    @ApiModelProperty(value = "减免事项")
    @FilterField("taxPreferenceItem")
    private List<String> taxPreferenceItems;

    @ApiModelProperty(value = "纳税人登记注册类型码值")
    @FilterField("taxpayerRegisterType.codeValue")
    private String taxpayerRegisterTypeCode;

    @ApiModelProperty(value = "动态筛选条件")
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
        if (CollectionUtils.isEmpty(conditions)) {
            conditions = null;
        } else {
            conditions = conditions.stream()
                    .filter(Objects::nonNull)
                    .filter(conditionQuery -> StringUtils.isNotBlank(conditionQuery.getConditionName()))
                    .distinct()
                    .filter(conditionQuery -> {
                        // 过滤处理为空值的条件
                        List<String> conditionValues = conditionQuery.getConditionValues();
                        if (CollectionUtils.isEmpty(conditionValues)) {
                            return false;
                        }
                        List<String> collect = conditionValues.stream()
                                .filter(Objects::nonNull)
                                .distinct()
                                .filter(StringUtils::isNotBlank)
                                .collect(Collectors.toList());
                        if (collect.isEmpty()) {
                            return false;
                        }
                        conditionQuery.setConditionValues(collect);
                        return true;
                    })
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(conditions)) {
                conditions = null;
            }
        }
    }

    @Data
    public static class ConditionQuery {
        /**
         * 条件名称
         */
        @ApiModelProperty(value = "条件名称")
        private String conditionName;

        /**
         * 具体要求
         */
        @ApiModelProperty(value = "具体要求集合")
        private List<String> conditionValues;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ConditionQuery) || conditionName == null) {
                return false;
            }
            return conditionName.equals(((ConditionQuery) obj).getConditionName());
        }
    }
}
