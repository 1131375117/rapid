package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPopularQaDataDO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhaoqiankun
 * @date 2021/12/29
 *         爬虫库 热门回答联合DTO
 */
@Accessors(chain = true)
@Data
public class SpiderPopularQaDataCombineDTO {

    /**
     * 爬虫库 热门回答对象
     */
    private SpiderPopularQaDataDO spiderPopularQaDataDO;
}
