package cn.huacloud.taxpreference.config;

import cn.huacloud.taxpreference.common.enums.BizCode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangkh
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tax-preference.sys")
public class SysConfig {

    /**
     * 系统管理密码
     */
    private String password = "12345678Aa";

    /**
     * 检查系统密码
     * @param password 系统密码
     */
    public void checkSysPassword(String password) {
        if (!this.password.equals(password)) {
            throw BizCode._4402.exception();
        }
    }

}
