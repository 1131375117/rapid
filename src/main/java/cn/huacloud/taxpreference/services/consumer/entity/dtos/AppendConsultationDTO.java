package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description 热门咨询DTO
 * @author fuhua
 * @date 2022-02-17
 */
@Data
public class AppendConsultationDTO {

    /**
    * id
    */
    @ApiModelProperty("初始问题id:consultationId")
    @NotNull(message = "不能为空")
    private Long consultationId;


    @ApiModelProperty("咨询内容")
    @NotEmpty(message = "咨询内容不能为空")
    private String content;

    /**
     * 咨询图片
     */
    @ApiModelProperty("咨询图片url")
    private List<String> imageUris;

}