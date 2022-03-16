package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.ConsultationDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryConsultationDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.QueryConsultationVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 案例数据mapper
 *
 * @author fuhua
 **/
@Repository
public interface ConsultationMapper extends BaseMapper<ConsultationDO> {
    /**
     * 查询热点咨询分页列表
     * @param queryConsultationDTO
     * @return
     */
    List<QueryConsultationVO> queryConsultationList(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize, @Param("queryDTO") QueryConsultationDTO queryConsultationDTO);
    /**
     * 查询热点咨询总数
     * @param queryConsultationDTO
     * @return
     */
    Long  selectCountByConsultationId(@Param("queryDTO")QueryConsultationDTO queryConsultationDTO);
}
