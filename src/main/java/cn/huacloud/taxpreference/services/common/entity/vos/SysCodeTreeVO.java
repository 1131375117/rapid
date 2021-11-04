package cn.huacloud.taxpreference.services.common.entity.vos;

import cn.huacloud.taxpreference.common.entity.vos.TreeVO;
import cn.huacloud.taxpreference.common.enums.SysCodeStatus;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 系统码值视图
 * @author wangkh
 */
@ApiModel("系统码值树形视图")
@Data
public class SysCodeTreeVO implements TreeVO<SysCodeTreeVO> {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("父主键ID")
    private Long pid;

    @ApiModelProperty("代码名称")
    private String codeName;

    @ApiModelProperty("代码码值")
    private String codeValue;

    @ApiModelProperty("代码类型")
    private SysCodeType codeType;

    @ApiModelProperty("代码状态")
    private SysCodeStatus codeStatus;

    @ApiModelProperty("扩展属性1")
    private String extendsField1;

    @ApiModelProperty("扩展属性2")
    private String extendsField2;

    @ApiModelProperty("是否为叶子节点")
    private Boolean leaf;

    @ApiModelProperty("孩子节点")
    private List<SysCodeTreeVO> children;
}
