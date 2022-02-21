package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.enums.ReplyStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 税收优惠列表DTO
 * @author: fuhua
 * @create: 2021-10-22 14:29
 **/
@Data
public class QueryConsultationDTO extends KeywordPageQueryDTO {

    @ApiModelProperty("税种")
    private List<String> taxCategoriesCodes;

    @ApiModelProperty("所属税务实务名称")
    private String taxPractices;

    @ApiModelProperty("回复状态NOT_REPLY(未答复), HAVE_REPLY(已答复)")
    private ReplyStatus status;
    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    /**
     * 截止时间
     */
    @ApiModelProperty("截止时间")
    private LocalDateTime endTime;


}
