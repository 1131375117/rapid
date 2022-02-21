package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 文档类型
 * @author wangkh
 */
public enum DocType implements IEnum<String>, SysCodeGetter {

    POLICIES("政策法规", "policies",ElasticsearchIndexConfig::getPolicies),
    POLICIES_EXPLAIN("政策解读", "policies_explain", ElasticsearchIndexConfig::getPoliciesExplain),
    FREQUENTLY_ASKED_QUESTION("热门问答", "frequently_asked_question", ElasticsearchIndexConfig::getFrequentlyAskedQuestion),
    TAX_PREFERENCE("税收优惠", "tax_preference", ElasticsearchIndexConfig::getTaxPreference),
    CASE_ANALYSIS("案例分析", "other_doc", ElasticsearchIndexConfig::getOtherDoc),
    CONSULTATION("专家咨询","consultation",ElasticsearchIndexConfig::getConsultation);
    private static Map<String, DocType> indexPrefixDocTypeMap;

    /**
     * 名称
     */
    @Getter
    public final String name;

    public final String indexPrefix;

    /**
     * ES索引获取器
     */
    public final Function<ElasticsearchIndexConfig, ElasticsearchIndexConfig.Index> indexGetter;

    DocType(String name, String indexPrefix, Function<ElasticsearchIndexConfig, ElasticsearchIndexConfig.Index> indexGetter) {
        this.name = name;
        this.indexPrefix = indexPrefix;
        this.indexGetter = indexGetter;
    }

    @Override
    public String getValue() {
        return name();
    }

    /**
     * 根据ES索引名称获取文档类型
     * @param index 名称
     * @return 文档类型
     */
    public static DocType getDocTypeByIndex(String index) {
        String indexPrefix = StringUtils.substringBeforeLast(index, "_index");
        if (indexPrefixDocTypeMap == null) {
            indexPrefixDocTypeMap = new LinkedHashMap<>();
            for (DocType value : values()) {
                if (indexPrefixDocTypeMap.containsKey(value.indexPrefix)) {
                    continue;
                }
                indexPrefixDocTypeMap.put(value.indexPrefix, value);
            }
        }
        return indexPrefixDocTypeMap.get(indexPrefix);
    }
}
