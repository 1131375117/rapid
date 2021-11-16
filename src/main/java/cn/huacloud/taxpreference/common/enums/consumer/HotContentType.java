package cn.huacloud.taxpreference.common.enums.consumer;

import cn.huacloud.taxpreference.common.enums.SysCodeGetter;
import lombok.Getter;

/**
 * @author wangkh
 */
public enum  HotContentType implements SysCodeGetter {

    POLICIES("政策法规"),
    POLICIES_EXPLAIN("政策解读"),
    FREQUENTLY_ASKED_QUESTION("热门问答"),
    TAX_PREFERENCE("税收优惠");

    @Getter
    public final String name;

    HotContentType(String name) {
        this.name = name;
    }
}
