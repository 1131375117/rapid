package cn.huacloud.taxpreference.services.common.entity.dtos;

import cn.huacloud.taxpreference.common.enums.SysCodeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangkh
 */
@Getter
@Setter
public class SysCodeQueryDTO {
    @ApiModelProperty("码值类型")
    private SysCodeType sysCodeType;

    @ApiModelProperty("父主键ID")
    private Long pid;

    /**
     * 参数合理化
     */
    public void paramReasonable() {
        if (pid == null) {
            pid = 0L;
        }
    }
}
