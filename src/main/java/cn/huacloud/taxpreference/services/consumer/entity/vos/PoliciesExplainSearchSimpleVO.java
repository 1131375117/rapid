package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author wangkh
 */
@Data
public class PoliciesExplainSearchSimpleVO {
    @ApiModelProperty(value = "主键ID",example = "3051")
    private Long id;

    @ApiModelProperty(value = "标题",example = "这是标题")
    private String title;

    @ApiModelProperty(value = "发布日期",example = "2022-01-18")
    private LocalDate releaseDate;
    @ApiModelProperty(value = "文档来源",example = "sss")
    private String docSource;
    @ApiModelProperty("税种")
    private List<SysCodeSimpleVO> taxCategories;
}
