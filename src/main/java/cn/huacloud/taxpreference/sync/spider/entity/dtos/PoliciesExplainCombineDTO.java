package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class PoliciesExplainCombineDTO {
    private PoliciesExplainDO policiesExplainDO;
    private List<AttachmentDO> attachmentDOList;
}
