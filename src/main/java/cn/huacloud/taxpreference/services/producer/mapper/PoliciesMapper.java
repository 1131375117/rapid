package cn.huacloud.taxpreference.services.producer.mapper;

import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesDO;
import cn.huacloud.taxpreference.services.producer.entity.dtos.QueryPoliciesDTO;
import cn.huacloud.taxpreference.services.producer.entity.vos.PoliciesVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 政策法规
 *
 * @author wuxin
 */
@Repository
public interface PoliciesMapper extends BaseMapper<PoliciesDO> {

    /**
     *
     * @param page
     * @param queryPoliciesDTO
     * @param sort
     * @return
     */
    IPage<PoliciesVO> queryPoliciesVOList(@Param("page") Page<PoliciesVO> page, @Param("page")QueryPoliciesDTO queryPoliciesDTO, @Param("page")String sort);

}
