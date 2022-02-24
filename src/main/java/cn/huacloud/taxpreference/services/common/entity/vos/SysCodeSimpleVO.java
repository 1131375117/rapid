package cn.huacloud.taxpreference.services.common.entity.vos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统码值简单视图
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class SysCodeSimpleVO {
    /**
     * 码值名称
     */
    @ApiModelProperty(value = "码值名称",example = "码值")
    private String codeName;
    /**
     * 码值代码
     */
    @ApiModelProperty(value = "码值代码",example = "01")
    private String codeValue;
}
