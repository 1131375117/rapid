package cn.huacloud.taxpreference.config;

import cn.hutool.core.thread.FinalizableDelegatedExecutorService;
import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.index.Index;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Index {
        private String index;
        private String alias;
    }
}
