package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * dto
 *
 * @author fuhua
 **/
@Data
public class OtherDocDTO extends AbstractHighlightPageQueryDTO{
    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getOtherDoc().getAlias();
    }

    @Override
    public List<String> searchFields() {
        return Arrays.asList("title","plainContent");
    }

}
