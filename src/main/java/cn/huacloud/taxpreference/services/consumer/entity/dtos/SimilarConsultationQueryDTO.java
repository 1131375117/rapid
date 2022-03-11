package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * dto
 *
 * @author fuhua
 **/
@Data
public class SimilarConsultationQueryDTO extends AbstractHighlightPageQueryDTO {

    @ApiModelProperty("所属税种")
    @FilterField("taxCategories.codeValue")
    private List<String> taxCategoriesCodes;

    @ApiModelProperty("税务实务")
    @FilterField("taxPractices")
    private List<String> taxPractices;

    @ApiModelProperty("所属行业")
    @FilterField("industries.codeValue")
    private List<String> industries;

    @ApiModelProperty(value = "公开",hidden = true)
    @FilterField("published")
    private Long published;


    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getConsultation().getAlias();
    }

    @Override
    public List<String> searchFields() {
        return Collections.singletonList("consultationContent.content");
    }

    @Override
    public List<SortBuilder<?>> sortBuilders() {
        return Arrays.asList(SortBuilders.fieldSort("finishTime").order(SortOrder.DESC), SortBuilders.fieldSort("_id").order(SortOrder.DESC));
    }


}
