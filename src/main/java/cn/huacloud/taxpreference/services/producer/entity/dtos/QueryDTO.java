package cn.huacloud.taxpreference.services.producer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuxin
 */
@Data
@ApiModel
public class QueryDTO extends KeywordPageQueryDTO {

    @ApiModelProperty("查询标题")
    private String title;

}
