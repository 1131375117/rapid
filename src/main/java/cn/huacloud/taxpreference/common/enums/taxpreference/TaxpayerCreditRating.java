package cn.huacloud.taxpreference.common.enums.taxpreference;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 纳税人信用等级
 * @author wangkh
 */
public enum TaxpayerCreditRating implements IEnum<String> {

    /**
     * 不限
     */
    UNLIMITED,
    /**
     * 信用等级
     */
    A, B, C, D, M;

    @Override
    public String getValue() {
        return this.name();
    }
}
