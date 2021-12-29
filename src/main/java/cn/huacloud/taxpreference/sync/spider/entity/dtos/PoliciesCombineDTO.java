package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import lombok.Data;

import java.util.List;

/**
 * @author wangkh
 */
@Data
public class PoliciesCombineDTO {
    private PoliciesDO policiesDO;
    private List<AttachmentDO> attachmentDOList;
}
