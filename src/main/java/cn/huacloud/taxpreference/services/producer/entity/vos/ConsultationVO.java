package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 咨询问题返回VO对象
 *
 * @author fuhua
 **/
@Data
public class ConsultationVO {
    /**
     * 所属税种码值
     */
    @ApiModelProperty("所属税种码值list")
    private List<String> taxCategoriesCodes;

    @ApiModelProperty("所属税种码值名称")
    private List<String> taxCategoriesNames;

    @ApiModelProperty("所属税务实务名称")
    private String taxPractices;

    @ApiModelProperty("咨询用户")
    private Long customerUserId;
    @ApiModelProperty("咨询用户")
    private String customerUserName;

    @ApiModelProperty("咨询时间")
    private LocalDateTime createTime;

    @ApiModelProperty("所属行业标签code集合")
    private List<String> industryCodes;

    @ApiModelProperty("所属行业标签name集合")
    private List<String> industryNames;

    @ApiModelProperty("是否公开(0-不公开,1-公开)")
    private Long published;
    private List<ConsultationContentVO> consultationContentVO;

}
