package cn.huacloud.taxpreference.sync.spider.entity.dos;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Data
public class SpiderPolicyAttachmentDO {
    private Long id;
    private String uniqueId;
    private String attachmentType;
    private String docId;
    private String attachmentName;
    private String path;
    private String pathUrl;
    private String downUrl;
    private LocalDateTime createTime;
}
