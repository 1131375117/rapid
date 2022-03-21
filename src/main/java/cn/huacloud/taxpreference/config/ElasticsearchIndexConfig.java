package cn.huacloud.taxpreference.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ES索引配置
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tax-preference.index")
public class ElasticsearchIndexConfig {

    private Index policies = new Index("policies_index", "policies");

    private Index policiesExplain = new Index("policies_explain_index", "policies_explain");

    private Index frequentlyAskedQuestion = new Index("frequently_asked_question_index", "frequently_asked_question");

    private Index taxPreference = new Index("tax_preference_index", "tax_preference");

    private Index otherDoc = new Index("other_doc_index", "other_doc");

    private Index consultation = new Index("consultation_index", "consultation");

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Index {
        private String index;
        private String alias;
    }
}
