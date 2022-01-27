package cn.huacloud.taxpreference.sync.spider.entity.dos;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangkh
 */
@Data
public class SpiderPopularQaDataDO {

    /**
     * 热门问答-id
     */
    private String id;
    /**
     * 区域
     */
    private String region;
    /**
     * 热门问答-url
     */
    private String url;
    /**
     * 热门问答-来源
     */
    private String contentSource;
    /**
     * 热门问答-发布日期
     */
    private String publishDate;
    /**
     * 热门问答-标题
     */
    private String title;
    /**
     * 热门问答-标题md5值
     */
    private String titleMd5;
    /**
     * 热门问答-正文内容
     */
    private String content;
    /**
     * 热门问答-正文内容md5
     */
    private String contentMd5;
    /**
     * 热门问答-源码
     */
    private String html;
    /**
     * 网站名称
     */
    private String siteName;
    /**
     * 爬取时间
     */
    private LocalDateTime spiderTime;
    /**
     * 重复标记(默认为0不重复, 为1时重复)
     */
    private String repeatMark;
    /**
     * 查重复时间
     */
    private LocalDateTime checkRepeatDate;
    /**
     * 重复id
     */
    private String repeatId;
    /**
     * 标题md5+政策正文内容md5的md5值
     */
    private String titleContentMd5;
    private String className;
    private String caseType;
}
