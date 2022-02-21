package cn.huacloud.taxpreference.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * 附件类型
 * @author wangkh
 */
public enum AttachmentType implements IEnum<String> {

    POLICIES("政策法规", "policies"),
    POLICIES_EXPLAIN("政策解读", "policies-explain"),
    FREQUENTLY_ASKED_QUESTION("热门问答", "faq"),
    CONSULTATION("专家咨询", "consultation"),
    CASE_ANALYSIS("案例分析", "case");

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
        if(attachmentName.contains(extension)){
            return pathPrefix + "/" + md5 + "_" + attachmentName;
        }
        return pathPrefix + "/" + md5 + "_" + attachmentName + "." + extension;
    }
}
