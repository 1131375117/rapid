package cn.huacloud.taxpreference.services.common.entity.dtos;

import lombok.Data;

/**
 * 字符串系统码值传输对象
 * @author wangkh
 */
@Data
public class SysCodeStringDTO {
    /**
     * 名称拼接字符串
     */
    private String names;
    /**
     * 码值拼接字符串
     */
    private String codes;
}
