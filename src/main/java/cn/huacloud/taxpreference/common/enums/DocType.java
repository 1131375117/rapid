package cn.huacloud.taxpreference.common.enums;

import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import com.baomidou.mybatisplus.annotation.IEnum;

import java.util.function.Function;

/**
 * 文档类型
 * @author wangkh
 */
public enum DocType implements IEnum<String> {

    POLICIES("政策法规", ElasticsearchIndexConfig::getPolicies),
    POLICIES_EXPLAIN("政策解读", ElasticsearchIndexConfig::getPoliciesExplain),
    FREQUENTLY_ASKED_QUESTION("热门问答", ElasticsearchIndexConfig::getFrequentlyAskedQuestion),
    TAX_PREFERENCE("税收优惠", ElasticsearchIndexConfig::getTaxPreference),
    CASE_ANALYSIS("案例分析", ElasticsearchIndexConfig::getOtherDoc);

    /**
     * 名称
     */
    public final String name;

    /**
     * ES索引获取器
     */
    public final Function<ElasticsearchIndexConfig, ElasticsearchIndexConfig.Index> indexGetter;

    DocType(String name, Function<ElasticsearchIndexConfig, ElasticsearchIndexConfig.Index> indexGetter) {
        this.name = name;
        this.indexGetter = indexGetter;
    }

    @Override
    public String getValue() {
        return name();
    }
}
