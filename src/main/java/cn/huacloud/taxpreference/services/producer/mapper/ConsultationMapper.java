package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.ConsultationDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryConsultationDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryConsultationVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 案例数据mapper
 *
 * @author fuhua
 **/
@Repository
public interface ConsultationMapper extends BaseMapper<ConsultationDO> {
    /**
     * 查询热点咨询分页列表
     * @param page
     * @param queryConsultationDTO
     * @return
     */
    IPage<QueryConsultationVO> queryConsultationList(@Param("page") Page<QueryConsultationVO> page,@Param("queryDTO") QueryConsultationDTO queryConsultationDTO);
    /**
     * 查询热点咨询总数
     * @param queryConsultationDTO
     * @return
     */
    Long  selectCountByConsultationId(@Param("queryDTO")QueryConsultationDTO queryConsultationDTO);
}
