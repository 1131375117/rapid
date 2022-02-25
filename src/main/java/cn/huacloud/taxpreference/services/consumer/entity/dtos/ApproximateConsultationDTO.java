package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author fuhua
 **/
@Data
public class ApproximateConsultationDTO extends PageQueryDTO {

    /**
     * 适用行业码值
     */
    @ApiModelProperty(value = "所属行业标签集合",example = "[\"011\"]")
    private List<String> industryCodes;
}
