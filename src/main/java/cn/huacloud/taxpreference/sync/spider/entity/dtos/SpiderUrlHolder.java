package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * 爬虫数据连接地址获取器
 * @author wangkh
 */
@Getter
@Setter
public class SpiderUrlHolder implements SpiderUrlGetter {

    /**
     * 爬虫数据连接地址
     */
    private String spiderUrl;
}
