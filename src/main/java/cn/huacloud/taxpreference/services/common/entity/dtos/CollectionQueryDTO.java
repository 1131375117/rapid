package cn.huacloud.taxpreference.services.common.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.enums.CollectionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fuhua
 **/
@Data
public class CollectionQueryDTO extends PageQueryDTO {

    @ApiModelProperty("收藏类型")
    private CollectionType collectionType;
}
