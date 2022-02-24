package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author wangkh
 */
@Data
public class DocSearchSimpleVO {
    @ApiModelProperty(value = "主键ID",example = "182")
    private Long id;

    @ApiModelProperty(value = "标题",example = "国家需要重点扶持的高新技术企业减按15％的税率征收企业所得税")
    private String title;

    @ApiModelProperty(value = "发布日期",example = "2022-01-13")
    private LocalDate releaseDate;
}
