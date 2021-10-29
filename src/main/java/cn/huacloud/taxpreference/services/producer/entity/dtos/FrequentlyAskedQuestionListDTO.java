package cn.huacloud.taxpreference.services.producer.entity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * 热点问答数据传输对象
 *
 * @author wuxin
 */
@Data
@ApiModel
public class FrequentlyAskedQuestionListDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("政策Id集合")
    private String policiesIds;

    @ApiModelProperty("问题")
    private String title;

    @ApiModelProperty("回答")
    private String content;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;

}
