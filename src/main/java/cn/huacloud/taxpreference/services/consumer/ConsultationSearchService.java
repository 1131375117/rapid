package cn.huacloud.taxpreference.services.consumer;

import cn.huacloud.taxpreference.common.entity.dtos.PageQueryDTO;
import cn.huacloud.taxpreference.common.entity.vos.PageVO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ApproximateConsultationDTO;
import cn.huacloud.taxpreference.services.consumer.entity.dtos.ConsultationQueryDTO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationCountVO;
import cn.huacloud.taxpreference.services.consumer.entity.vos.ConsultationESVO;

import java.io.IOException;

/**
 * 热门咨询检索
 * @author fuhua
 **/
public interface ConsultationSearchService extends SearchService<ConsultationQueryDTO, ConsultationESVO> {
    /**
     * 热门热门咨询
     * @param pageQuery 查询条件
     * @return
     * @throws Exception
     */
    PageVO<ConsultationESVO> hotConsultation(PageQueryDTO pageQuery) throws Exception;

    /**
     * 最新的热门咨询
     * @param pageQuery
     * @return
     * @throws Exception
     */
    PageVO<ConsultationESVO> latestConsultation(PageQueryDTO pageQuery) throws Exception;

    /**
     * 热门咨询详情
     * @param id 热门咨询id
     * @return
     * @throws IOException
     */
    ConsultationESVO getConsultationDetails(Long id) throws IOException;

    /**
     * 统计文档
     * @return
     * @throws Exception
     */
    ConsultationCountVO getCount() throws Exception;

    /**
     * 近似热门咨询
     * @param approximateConsultationDTO 查询条件
     * @return
     * @throws Exception
     */
    PageVO<ConsultationESVO> approximateConsultation(ApproximateConsultationDTO approximateConsultationDTO) throws Exception;
}
