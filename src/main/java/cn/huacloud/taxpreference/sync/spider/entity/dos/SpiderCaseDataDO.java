package cn.huacloud.taxpreference.sync.spider.entity.dos;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author fuhua
 **/
@Data
public class SpiderCaseDataDO {
    private String uuid;
    private String url;
    private String caseType;
    private String title;
    private String contentSource;
    private String publishTime;
    private String contentText;
    private String contentHtml;
    private String html;
    private LocalDateTime spiderTime;
    private String siteName;
    private String titleContentMd5;
}
