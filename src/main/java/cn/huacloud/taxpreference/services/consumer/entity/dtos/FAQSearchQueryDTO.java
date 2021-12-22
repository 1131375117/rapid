package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
@Getter
@Setter
public class FAQSearchQueryDTO extends AbstractHighlightPageQueryDTO {

    /**
     * 热门问答来源
     */
    @FilterField("docSource")
    private String docSource;

    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getFrequentlyAskedQuestion().getAlias();
    }

}
