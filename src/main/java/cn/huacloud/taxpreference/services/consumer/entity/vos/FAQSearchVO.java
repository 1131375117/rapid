package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author wangkh
 */
@Data
public class FAQSearchVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;

    @ApiModelProperty("无装饰的组合文本")
    private String combinePlainContent;

    @ApiModelProperty("上一篇、下一篇")
    PreviousNextVO<Long> previousNext;

    @ApiModelProperty("浏览量")
    private Long views;

    @ApiModelProperty("收藏量")
    private Long collections;

}
