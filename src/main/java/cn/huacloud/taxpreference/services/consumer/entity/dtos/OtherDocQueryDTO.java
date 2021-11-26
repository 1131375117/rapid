package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * dto
 *
 * @author fuhua
 **/
@Data
public class OtherDocQueryDTO extends AbstractHighlightPageQueryDTO {

    @ApiModelProperty(value = "文档类型", notes = "目前只能支持 CASE_ANALYSIS 案例分析")
    private DocType docType;

    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getOtherDoc().getAlias();
    }

    @Override
    public List<String> searchFields() {
        return Collections.singletonList("title");
    }

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (docType == null) {
            docType = DocType.CASE_ANALYSIS;
        }
    }
}
