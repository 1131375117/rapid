package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationCountVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationESVO;

import java.io.IOException;

/**
 * 热门咨询检索
 * @author fuhua
 **/
public interface ConsultationSearchService extends SearchService<ConsultationQueryDTO, ConsultationESVO> {

    PageVO<ConsultationESVO> hotConsultation(PageQueryDTO pageQuery) throws Exception;

    PageVO<ConsultationESVO> latestConsultation(PageQueryDTO pageQuery) throws Exception;

    ConsultationESVO getConsultationDetails(Long id) throws IOException;


    ConsultationCountVO getCount() throws Exception;
}
