package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 附件类型
 * @author wangkh
 */
public enum AttachmentType implements IEnum<String> {

    POLICIES("政策法规");

    public final String name;

    AttachmentType(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
