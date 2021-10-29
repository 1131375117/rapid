package cn.huacloud.taxpreference.services.common.entity.dos;

import cn.huacloud.taxpreference.common.enums.SysCodeStatus;
import cn.huacloud.taxpreference.common.enums.SysCodeType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统码值数据实体
 * @author wangkh
 */
@Accessors(chain = true)
@Data
@TableName("t_sys_code")
public class SysCodeDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 父主键ID
     */
    private Long pid;
    /**
     * 代码名称
     */
    private String codeName;
    /**
     * 代码码值
     */
    private String codeValue;
    /**
     * 代码类型
     */
    private SysCodeType codeType;
    /**
     * 代码状态
     */
    private SysCodeStatus codeStatus;
    /**
     * 备注
     */
    private String note;
    /**
     * 扩展属性1
     */
    private String extendsField1;
    /**
     * 扩展属性2
     */
    private String extendsField2;
    /**
     * 是否为叶子节点
     */
    private Boolean leaf;
    /**
     * 排序字段
     */
    private Long sort;
}
