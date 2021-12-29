package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyAttachmentDO;
import cn.huacloud.taxpreference.sync.spider.entity.dos.SpiderPolicyDataDO;
import lombok.Data;

import java.util.List;

/**
 * @author wangkh
 */
@Data
public class SpiderPolicyCombineDTO {
    private SpiderPolicyDataDO spiderPolicyDataDO;
    private List<SpiderPolicyAttachmentDO> spiderPolicyAttachmentDOList;
}
