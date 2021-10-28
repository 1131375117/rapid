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
@ApiModel("系统码值视图")
@Data
public class SysCodeTreeVO implements TreeVO<SysCodeTreeVO> {
    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Long id;
    /**
     * 父主键ID
     */
    @ApiModelProperty("父主键ID")
    private Long pid;
    /**
     * 代码名称
     */
    @ApiModelProperty("代码名称")
    private String codeName;
    /**
     * 代码码值
     */
    @ApiModelProperty("代码码值")
    private String codeValue;
    /**
     * 代码类型
     */
    @ApiModelProperty("代码类型")
    private SysCodeType codeType;
    /**
     * 代码状态
     */
    @ApiModelProperty("代码状态")
    private SysCodeStatus codeStatus;
    /**
     * 是否为叶子节点
     */
    @ApiModelProperty("是否为叶子节点")
    private Boolean leaf;

    @ApiModelProperty("孩子节点")
    private List<SysCodeTreeVO> children;
}
