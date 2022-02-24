package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.annotations.FilterField;
import cn.huacloud.taxpreference.config.ElasticsearchIndexConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
@Getter
@Setter
public class FAQSearchQueryDTO extends AbstractHighlightPageQueryDTO {

    /**
     * 解答机构
     */
    @FilterField("answerOrganization")
    @ApiModelProperty(value = ",通过热门问答来源获取",example = "国家税务总局辽宁省税务局")
    private String answerOrganization;
    /**
     * 机构类型
     */
    @FilterField("organizationType")
    @ApiModelProperty(value = "机构类型,通过热门问答来源获取",notes = "官方机关")
    private String organizationType;
    /**
     * 主题分类
     */
    @FilterField("subjectType")
    @ApiModelProperty(value = "主题分类",example = "税收实务")
    private String subjectType;

    @Override
    public String index(ElasticsearchIndexConfig config) {
        return config.getFrequentlyAskedQuestion().getAlias();
    }

}
