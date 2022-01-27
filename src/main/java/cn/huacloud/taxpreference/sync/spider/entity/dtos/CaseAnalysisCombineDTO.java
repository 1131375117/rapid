package cn.huacloud.taxpreference.sync.spider.entity.dtos;

import cn.huacloud.taxpreference.services.common.entity.dos.AttachmentDO;
import cn.huacloud.taxpreference.services.producer.entity.dos.OtherDocDO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wangkh
 */
@Accessors(chain = true)
@Data
public class CaseAnalysisCombineDTO {
    private OtherDocDO otherDocDO;
    private List<AttachmentDO> attachmentDOList;

}
