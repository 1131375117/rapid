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
public class ConsultationDTO  {

    /**
    * id
    */
    @ApiModelProperty("id")
    private Long id;

    /**
    * 所属税种码值
    */
    @ApiModelProperty("所属税种码值list")
    private List<String> taxCategoriesCodes;

    /**
     * 客户用户id
     */
    @ApiModelProperty("咨询客户id")
    private Long customerUserId;
    /**
    * 所属税务实务名称
    */
    @ApiModelProperty("所属税务实务名称")
    private String taxPractices;

    /**
    * 适用行业码值
    */
    @ApiModelProperty("所属行业标签集合")
    private List<String> industryCodes;

    @ApiModelProperty("咨询内容")
    @NotEmpty(message = "咨询内容不能为空")
    private String content;

    /**
     * 咨询图片
     */
    @ApiModelProperty("咨询图片url")
    private List<String> imageUris;
    /**
     * 是否公开
     */
    @ApiModelProperty("是否公开,0-不公开，1")
    @NotNull(message = "不能为空")
    private Long published;

}