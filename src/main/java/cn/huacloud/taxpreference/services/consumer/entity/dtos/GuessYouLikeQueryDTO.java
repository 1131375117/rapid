package cn.huacloud.taxpreference.services.consumer.entity.dtos;

import cn.huacloud.taxpreference.common.entity.dtos.KeywordPageQueryDTO;
import cn.huacloud.taxpreference.common.enums.DocType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 猜你喜欢分页查询对象
 * @author wangkh
 */
@Getter
@Setter
public class GuessYouLikeQueryDTO extends KeywordPageQueryDTO {
    @ApiModelProperty("用户ID")
    private Long userId;
    @ApiModelProperty("文档类型")
    private DocType docType;
}
