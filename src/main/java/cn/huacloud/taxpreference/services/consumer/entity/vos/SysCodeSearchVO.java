package cn.huacloud.taxpreference.services.consumer.entity.vos;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统码值ES视图
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class SysCodeSearchVO {
    /**
     * 码值名称
     */
    private String codeName;
    /**
     * 码值代码
     */
    private String codeValue;
}
