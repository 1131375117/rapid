package cn.huacloud.taxpreference.services.producer.entity.vos;

import cn.huacloud.taxpreference.common.enums.ReplyStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询咨询问题返回VO对象
 *
 * @author fuhua
 **/
@Data
public class QueryConsultationVO {
    private Long id;

    @ApiModelProperty("所属税种码值名称")
    private String taxCategoriesNames;

    @ApiModelProperty("所属税务实务名称")
    private String taxPractices;


    @ApiModelProperty("咨询时间")
    private LocalDateTime createTime;

    @ApiModelProperty("咨询内容")
    private String content;

    @ApiModelProperty("答复状态,  NOT_REPLY-未答复,HAVE_REPLY-已答复;")
    private ReplyStatus status;
}
