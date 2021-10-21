package cn.huacloud.taxpreference.common.entity.vos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页对象
 * @author wangkh
 */
@Data
@ApiModel
public class PageVO {

    @ApiModelProperty("当前页")
    private Integer pageNum;
    @ApiModelProperty("每页显示条数")
    private Integer pageSize;
    @ApiModelProperty("满足条件总行数")
    private Long total;


}
