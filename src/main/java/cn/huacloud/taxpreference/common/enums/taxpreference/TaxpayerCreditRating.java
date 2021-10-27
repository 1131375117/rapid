package cn.huacloud.taxpreference.common.enums.taxpreference;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 纳税人信用等级
 * @author wangkh
 */
public enum TaxpayerCreditRating implements IEnum<String> {

    A, B, C, D, M;

    @Override
    public String getValue() {
        return null;
    }
}
