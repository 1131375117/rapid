package cn.huacloud.taxpreference.common.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Getter
@Setter
public class ChoiceGroupVO<T> extends GroupVO<T> {
    @ApiModelProperty("是否多选")
    private Boolean multipleChoice;
}
