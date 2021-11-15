package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.constants.ValidationGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * 热点问答数据传输对象
 *
 * @author wuxin
 */
@Data
@ApiModel
public class FrequentlyAskedQuestionDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("政策Id集合")
    private String policiesIds;


    @NotEmpty(message = "问题不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    @ApiModelProperty("问题")
    private String title;

    @NotEmpty(message = "回答不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    @ApiModelProperty("回答")
    private String content;

    @NotEmpty(message = "来源不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    @ApiModelProperty("来源")
    private String docSource;

    @NotEmpty(message = "发布日期不能为空", groups = {ValidationGroup.Update.class, ValidationGroup.Create.class})
    @ApiModelProperty("发布日期")
    private LocalDate releaseDate;
    @ApiModelProperty("用户id")
    private Long inputUserId;



}
