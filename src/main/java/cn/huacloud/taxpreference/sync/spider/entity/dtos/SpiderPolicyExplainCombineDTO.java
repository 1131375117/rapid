package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyExplainDataDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class SpiderPolicyExplainCombineDTO {
    private SpiderPolicyExplainDataDO spiderPolicyExplainDataDO;
    private List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList;
}
