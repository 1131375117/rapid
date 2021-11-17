package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 猜你喜欢分页查询对象
 * @author wangkh
 */
@Data
public class GuessYouLikeQueryDTO extends KeywordPageQueryDTO {
    @ApiModelProperty("用户ID")
    private Long userId;
}
