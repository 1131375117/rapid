package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPopularQaDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderQaRealationDataDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author zhaoqiankun
 * @date 2021/12/29
 *         爬虫库 热门回答联合DTO
 */
@Accessors(chain = true)
@Data
public class SpiderPopularQaDataCombineDTO extends SpiderUrlHolder {

    /**
     * 爬虫库 热门回答对象
     */
    private SpiderPopularQaDataDO spiderPopularQaDataDO;

    /**
     * 爬虫库 附件对象list
     */
    private List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList;

    /**
     * 热门问答关联对象
     * */
    private SpiderQaRealationDataDO realationDataDO;

}
