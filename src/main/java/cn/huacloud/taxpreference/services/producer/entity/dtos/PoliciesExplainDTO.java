package cn.huacloud.taxpreference.services.producer.entity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * 政策解读数据传输对象
 *
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesExplainDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("来源")
    private String docSource;

    @ApiModelProperty("发布日期")
    private Date releaseDate;

    @ApiModelProperty("正文")
    private String content;

    @ApiModelProperty("政策法规id")
    private Long policiesId;
}