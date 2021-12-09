package cn.huacloud.taxpreference.services.consumer.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * 收藏DTO
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
public class CollectionVO {

    /**
     * 收藏类型
     */
    @ApiModelProperty("收藏类型")
    @NotEmpty(message = "收藏类型不能为空")
    private String collectionType;

    /**
     * 数据源id
     */
    @ApiModelProperty("数据源id")
    @NotEmpty(message = "收藏id不能为空")
    private Long sourceId;

    @ApiModelProperty("状态,true-代表已收藏,false代表未收藏")
    @NotEmpty(message = "收藏状态必传")
    private Boolean status;
}
