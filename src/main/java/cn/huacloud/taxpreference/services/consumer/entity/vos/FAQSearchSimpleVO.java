package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author wangkh
 */
@Data
public class FAQSearchSimpleVO {
    @ApiModelProperty(value = "主键ID", example = "4343")
    private Long id;

    @ApiModelProperty(value = "标题", example = "收到财政补贴如何进行税务处理")
    private String title;

    @ApiModelProperty(value = "来源", example = "上海税务")
    private String docSource;

    @ApiModelProperty(value = "发布日期", example = "2022-01-19")
    private LocalDate releaseDate;
}
