package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;

/**
 * @author wangkh
 */
public class FAQSearchSimpleVO {
    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;
}
