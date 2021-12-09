package cn.huacloud.taxpreference.services.common.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统参数表
 *
 * @author fuhua
 **/
@Accessors(chain = true)
@Data
@TableName("t_sys_param")
public class SysParamDO {
    /**
     * id
     */
    private Long id;
    /**
     * 参数类型
     */
    private String paramType;
    /**
     * 参数名字
     */
    private String paramName;
    /**
     * 参数key
     */
    private String paramKey;
    /**
     * 参数value
     */
    private String paramValue;
    /**
     * 参数说明
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
     * 参数状态
     */
    private String paramStatus;
}
