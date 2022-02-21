package cn.huacloud.taxpreference.services.producer.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @description 热门咨询DTO
 * @author fuhua
 * @date 2022-02-17
 */
@Data
public class ConsultationReplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    @ApiModelProperty("热门咨询id")
    @NotNull(message = "热门咨询id不能为空")
    private Long consultationId;


    @ApiModelProperty("答复内容")
    @NotEmpty(message = "热门咨询id不能为空")
    private String content;

    /**
     * 咨询图片
     */
    @ApiModelProperty("答复图片url")
    private List<String> imageUris;

    @ApiModelProperty("答复专家id")
    private Long professorUserId;

}