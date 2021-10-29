package cn.huacloud.taxpreference.services.common.entity.vos;

import cn.huacloud.taxpreference.common.enums.SysCodeStatus;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangkh
 */
@ApiModel("系统码值视图")
@Data
public class SysCodeVO {

    @ApiModelProperty("代码名称")
    private String codeName;

    @ApiModelProperty("代码码值")
    private String codeValue;

    @ApiModelProperty("代码类型")
    private SysCodeType codeType;

    @ApiModelProperty("代码状态")
    private SysCodeStatus codeStatus;

}
