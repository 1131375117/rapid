package cn.huacloud.taxpreference.sync.spider.entity.dos;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Data
public class SpiderPolicyDataDO {
    private String id;
    private String region;
    private String url;
    private String contentSource;
    private String taxClassName;
    private String title;
    private String titleMd5;
    private String documentNumber;
    private String publishDate;
    private String contentIsValid;
    private String content;
    private String contentMd5;
    private String html;
    private String siteName;
    private LocalDateTime spiderTime;
    private Boolean repeatMark;
    private LocalDateTime checkRepeatDate;
    private String repeatId;
    private String titleContentMd5;
    private String nextContent;
    private Boolean deleteMark;
}
