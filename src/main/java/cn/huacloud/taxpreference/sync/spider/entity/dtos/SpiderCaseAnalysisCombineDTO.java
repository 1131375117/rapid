package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderCaseDataDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class SpiderCaseAnalysisCombineDTO extends SpiderUrlHolder {
    private SpiderCaseDataDO spiderCaseDataDO;
    private List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList;
}
