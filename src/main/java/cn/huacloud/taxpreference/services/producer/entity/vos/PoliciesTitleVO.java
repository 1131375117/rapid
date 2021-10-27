package cn.huacloud.taxpreference.services.producer.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class PoliciesTitleVO {

    @ApiModelProperty("政策法规名称")
    private String title;
}
