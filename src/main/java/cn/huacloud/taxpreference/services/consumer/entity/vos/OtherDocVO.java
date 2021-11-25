package cn.huacloud.taxpreference.services.consumer.entity.vos;

import cn.huacloud.taxpreference.services.common.entity.vos.SysCodeSimpleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

/**
 * 案例表实体
 *
 * @author fuhua
 **/
@Data
@Accessors(chain = true)
public class OtherDocVO {
    private Long id;
    /**
     * 文档类型：CASE_ANALYSIS-案例分析
     */
    @ApiModelProperty("文档类型：CASE_ANALYSIS-案例分析")
    private SysCodeSimpleVO docType;
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;
    /**
     * 来源
     */
    @ApiModelProperty("文档来源")
    private String docSource;
    /**
     * 发布日期
     */
    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;
    /**
     * 富文本内容
     */
    @ApiModelProperty("富文本内容")
    private String htmlContent;

    @ApiModelProperty("上一篇、下一篇")
    PreviousNextVO<Long> previousNext;

}
