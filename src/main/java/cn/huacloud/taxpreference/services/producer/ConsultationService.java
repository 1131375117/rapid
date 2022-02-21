package cn.huacloud.taxpreference.services.producer;

import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.common.utils.ResultVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.ConsultationReplyDTO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryConsultationDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.ConsultationVO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryConsultationVO;

/**
 * 热门咨询
 *
 * @author fuhua
 **/
public interface ConsultationService {
    /**
     * 新增热门咨询
     * @param consultationDTO
     * @return
     */
    Boolean saveConsultation(ConsultationDTO consultationDTO);

    /**
     * 热门咨询答复
     * @param consultationReplyDTO
     */
    void replyConsultation(ConsultationReplyDTO consultationReplyDTO);

    /**
     * 热门咨询详情
     * @param id
     */
    ConsultationVO consultationDetail(Long id);

    ResultVO<PageVO<QueryConsultationVO>> queryConsultationList(QueryConsultationDTO queryConsultationDTO);
}
