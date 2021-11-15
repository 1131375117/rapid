package cn.huacloud.taxpreference.services.common.entity.vos;

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
    private String codeName;
    /**
     * 码值代码
     */
    private String codeValue;
}
