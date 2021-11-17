package cn.huacloud.taxpreference.common.entity.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 关键字分页查询对象
 *
 * @author wangkh
 */
@Getter
@Setter
@ApiModel
public class KeywordPageQueryDTO extends PageQueryDTO {

    @ApiModelProperty("查询关键字")
    private String keyword;

    @Override
    public void paramReasonable() {
        super.paramReasonable();
        if (StringUtils.isBlank(keyword)) {
            keyword = null;
        }
    }
}
