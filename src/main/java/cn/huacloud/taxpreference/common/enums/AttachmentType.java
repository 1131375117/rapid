package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 附件类型
 * @author wangkh
 */
public enum AttachmentType implements IEnum<String> {

    POLICIES("政策法规", "policies");

    public final String name;

    private final String pathPrefix;

    AttachmentType(String name, String pathPrefix) {
        this.name = name;
        this.pathPrefix = pathPrefix;
    }

    @Override
    public String getValue() {
        return this.name();
    }

    public String getPath(String md5, String attachmentName, String extension) {
        return pathPrefix + "/" + md5 + "_" + attachmentName + "." + extension;
    }
}
